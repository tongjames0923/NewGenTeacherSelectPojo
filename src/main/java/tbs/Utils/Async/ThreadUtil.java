package tbs.Utils.Async;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;
import tbs.Utils.Async.interfaces.AsyncToDo;
import tbs.Utils.Async.interfaces.AsyncToGet;
import tbs.Utils.Async.interfaces.IThreadLocker;
import tbs.Utils.Async.interfaces.IThreadSign;
import tbs.Utils.Results.AsyncResult;
import tbs.Utils.Results.AsyncWaitter;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Component
public class ThreadUtil {
    @Resource
    AsyncTaskExecutor executor;

    @Resource
    IThreadLocker threadLocker;


    private void before(IThreadSign sign) throws Exception {
        if (threadLocker.isLock(sign)) {
            throw new Exception("thread had being locked");
        }
        threadLocker.lock(sign);
    }

    private void after(IThreadSign sign) {
        threadLocker.unlock(sign);
    }

    private interface IWork {
        void work();
    }

    private void basicDo(IThreadSign sign, IWork work) throws Exception {
        before(sign);
        Exception ex = null;
        try {
            work.work();
        } catch (Exception e) {
            ex = e;
        } finally {
            after(sign);
        }
    }


    public AsyncWaitter doWithAsync( List<AsyncToDo> tasks) {
        List<AsyncResult> results=new LinkedList<>();
        for(AsyncToDo t:tasks)
        {
            if(t==null)
                continue;
            IThreadSign sign = SpringUtil.getBean(IThreadSign.class);
            IThreadLocker temp= SpringUtil.getBean(IThreadLocker.class);
            AsyncResult result = new AsyncResult(temp, sign);
            results.add(result);
        }
        return new AsyncWaitter(results,tasks,null,executor);
    }
    public AsyncWaitter doWithAsync(AsyncToDo... tasks)
    {
        return doWithAsync(Arrays.asList(tasks));
    }

    private class IndexFuture<T> {
        Future<T> future;
        int index;

        public IndexFuture(Future<T> future, int index) {
            this.future = future;
            this.index = index;
        }

        public Future<T> getFuture() {
            return future;
        }

        public void setFuture(Future<T> future) {
            this.future = future;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    public <T> List<T> getWithAsync(IThreadSign sign, Collection<AsyncToGet<T>> tasks) throws Exception {
        List<T> results = new ArrayList<>(tasks.size());
        List<IndexFuture<T>> futures = new LinkedList<>();
        basicDo(sign, new IWork() {
            @Override
            public void work() {
                int index = 0;
                for (AsyncToGet<T> t : tasks) {
                    futures.add(new IndexFuture<T>(executor.submit(new Callable<T>() {

                        @Override
                        public T call() throws Exception {
                            return t.getSomething();
                        }
                    }), index++));
                    results.add(null);
                }
                while (!futures.isEmpty()) {
                    Iterator<IndexFuture<T>> iterator = futures.iterator();
                    while (iterator.hasNext()) {
                        IndexFuture<T> f = iterator.next();
                        if (f.getFuture().isDone() || f.getFuture().isCancelled()) {
                            try {
                                results.set(f.getIndex(), f.getFuture().get());
                            } catch (Exception e) {
                                results.set(f.getIndex(), null);
                            }
                            iterator.remove();
                        }
                        index++;
                    }
                }
            }
        });
        return results;
    }


}
