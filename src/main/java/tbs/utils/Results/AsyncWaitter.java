package tbs.utils.Results;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.core.task.AsyncTaskExecutor;
import tbs.utils.Async.interfaces.AsyncToDo;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static tbs.utils.Results.AsyncTaskResult.DONE;
import static tbs.utils.Results.AsyncTaskResult.ERROR;

public class AsyncWaitter {
    List<AsyncTaskResult> asyncResults;
    List<AsyncToDo> tasks;

    private AsyncTaskExecutor executor;

    public AsyncWaitter(List<AsyncTaskResult> asyncResults, List<AsyncToDo> tasks, Event event, AsyncTaskExecutor executor) {
        this.asyncResults = asyncResults;
        this.tasks = tasks;
        this.executor = executor;
        this.event = event;
    }

    public static interface Event {
        void onSuccess(AsyncTaskResult result);

        default void onError(Exception e, String sign) {
            System.err.println(String.format("%s error!", sign));
            e.printStackTrace();
        }

        default void dataChange(AsyncTaskResult result) {

        }
    }

    Event event;

    CountDownLatch latch;

    public Event getEvent() {
        return event;
    }

    public AsyncWaitter setEvent(Event event) {
        this.event = event;
        return this;
    }

    public class ResultStatusor implements AsyncTaskResult.IDataChanged {
        private Event event;
        List<AsyncTaskResult> successList = new LinkedList<>();
        List<AsyncTaskResult> failList = new LinkedList<>();
        CountDownLatch latch;


        long total;

        public Event getEvent() {
            return event;
        }

        public void setEvent(Event event) {
            this.event = event;
        }

        public List<AsyncTaskResult> getSuccessList() {
            return successList;
        }

        public List<AsyncTaskResult> getFailList() {
            return failList;
        }

        public long getTotal() {
            return total;
        }

        public ResultStatusor(Event event, long total) {
            this.event = event;
            this.total = total;
            latch = new CountDownLatch((int) total);
        }

        public void waitForDone() throws Exception {
            latch.await(1, TimeUnit.MINUTES);
        }

        @Override
        public void changed(AsyncTaskResult self, long whatChanged) {
            if (whatChanged == STATUS_CHANGED) {
                if (self.getSTATUS() == DONE) {
                    if (event != null) {
                        event.onSuccess(self);
                    }
                    successList.add(self);
                }
                if (self.getSTATUS() == ERROR) {
                    if (event != null) {
                        event.onError(self.getException(), self.getSign().key());
                    }
                    failList.add(self);
                }
            }
            if (whatChanged == DATA_CHANGED) {
                if (event != null) {
                    event.dataChange(self);
                }
            }
        }
    }

    public ResultStatusor execute() {
        return execute(this.getEvent());
    }

    public ResultStatusor execute(Event e) {
        if (CollectionUtil.isEmpty(asyncResults)) {
            return null;
        }
        int index = 0;
        ResultStatusor statusChange = new ResultStatusor(e, asyncResults.size());
        for (AsyncTaskResult result : asyncResults) {
            result.setChangeEvent(statusChange);
            int finalIndex = index;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    result.getLocker().lock(result.getSign());
                    Exception ex=null;
                    try {
                        tasks.get(finalIndex).doSomething(result);
                        result.setSTATUS(DONE);
                    } catch (Exception e) {
                        result.setException(e);
                        result.setSTATUS(AsyncTaskResult.ERROR);
                    } finally {
                        result.getLocker().unlock(result.getSign());
                        statusChange.latch.countDown();
                    }
                }
            });
            index++;
        }
        return statusChange;
    }
}
