package tbs.utils.Async;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;
import tbs.utils.Async.interfaces.AsyncToDo;
import tbs.utils.Async.interfaces.AsyncToGet;
import tbs.utils.Async.interfaces.IThreadLocker;
import tbs.utils.Async.interfaces.IThreadSign;
import tbs.utils.Results.AsyncTaskResult;
import tbs.utils.Results.AsyncWaitter;


import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author abstergo
 */
@Component
@Slf4j
public class ThreadUtil {
    @Resource
    AsyncTaskExecutor executor;



    private void before(IThreadSign sign,IThreadLocker threadLocker) throws Exception {
        if (threadLocker.isLock(sign)) {
            throw new Exception("thread had being locked");
        }
        threadLocker.lock(sign);
    }

    private void after(IThreadSign sign,IThreadLocker threadLocker) {
        threadLocker.unlock(sign);
    }

    private interface IWork {
        void work();
    }

    private void basicDo(IThreadSign sign,IThreadLocker locker, IWork work) throws Exception {
        before(sign,locker);
        Exception ex = null;
        try {
            work.work();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            ex = e;
        } finally {
            after(sign,locker);
        }
    }

    public AsyncWaitter doWithAsync(List<AsyncToDo> tasks) {
        return doWithAsync(null, null, tasks);
    }

    public AsyncWaitter doWithAsync(IThreadSign sign, IThreadLocker locker, List<AsyncToDo> tasks) {
        List<AsyncTaskResult> results = new LinkedList<>();
        for (AsyncToDo t : tasks) {
            if (t == null) {
                continue;
            }
            if (sign == null) {
                sign = SpringUtil.getBean(IThreadSign.class);
            }
            if (locker == null) {
                locker = SpringUtil.getBean(IThreadLocker.class);
            }
            AsyncTaskResult result = new AsyncTaskResult(locker, sign);
            results.add(result);
            log.debug("ASYNC TASK CREATE "+sign.key());
        }
        return new AsyncWaitter(results, tasks, null, executor);
    }

    public AsyncWaitter doWithAsync(IThreadSign sign, IThreadLocker locker, AsyncToDo... tasks) {
        return doWithAsync(sign, locker, Arrays.asList(tasks));
    }

    public AsyncWaitter doWithAsync(AsyncToDo... tasks) {
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

    public <T> List<T> getWithAsync(Collection<AsyncToGet<T>> tasks) throws Exception {
        return getWithAsync(null,null,tasks);
    }

    public <T> List<T> getWithAsync(IThreadSign sign,IThreadLocker locker, Collection<AsyncToGet<T>> tasks) throws Exception {

        if (sign == null) {
            sign = SpringUtil.getBean(IThreadSign.class);
        }
        if (locker == null) {
            locker = SpringUtil.getBean(IThreadLocker.class);
        }
        List<T> results = new ArrayList<>(tasks.size());
        List<IndexFuture<T>> futures = new LinkedList<>();
        basicDo(sign, locker,new IWork() {
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
