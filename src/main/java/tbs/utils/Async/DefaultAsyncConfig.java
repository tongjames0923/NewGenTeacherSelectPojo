package tbs.utils.Async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import tbs.utils.AOP.controller.IAction;
import tbs.utils.Async.interfaces.*;
import tbs.utils.EncryptionTool;
import tbs.utils.Results.AsyncTaskResult;
import tbs.utils.Results.NetResult;
import tbs.utils.error.NetError;
import tbs.utils.redis.RedisConfig;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;

@Configuration
@EnableAsync
@EnableScheduling
@Slf4j
public class DefaultAsyncConfig {


    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean(IThreadSign.class)
    IThreadSign defaultSign() {
        return new IThreadSign() {

            String key = "asyncTaskSign_" + UUID.randomUUID().toString();

            @Override
            public String toString() {
                return key;
            }

            @Override
            public String key() {
                return key;
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(ILockProxy.class)
    ILockProxy iLockProxy(@Qualifier(RedisConfig.REDISSION_LOCK) ILocker default_locker) {
        return new ILockProxy() {

            @Override
            public Object run(FunctionWithThrows<ILocker,Object> f, String lockName) throws Throwable {
                IThreadSign threadSign = new IThreadSign() {
                    String key = "LOCK:" + lockName;

                    @Override
                    public String key() {
                        return key;
                    }
                };
                Object data = null;
                log.info("BEGIN LOCK "+threadSign.key());
                Throwable ex=null;
                locker.lock(threadSign);
                try {
                    data = f.apply(locker);
                } catch (Throwable e) {
                    ex=e;
                } finally {
                    locker.unlock(threadSign);
                    log.info("END LOCK "+threadSign.key());
                    if(ex!=null)
                        throw ex;
                }
                return data;
            }
            ILocker locker=default_locker;
            @Override
            public ILocker getLocker() {
                return locker;
            }

            @Override
            public void setLocker(ILocker lk) {
                this.locker=lk;
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(AsyncMethod.class)
    AsyncMethod asyncMethod() {
        return new AsyncMethod() {
            ConcurrentMap<String, NetResult.AsyncDelayResult> dataSource = new ConcurrentHashMap<>();

            @Override
            public String resultKey() {
                return "ASYNC_TASK:" + UUID.randomUUID().toString();
            }


            @Override
            public NetResult process(NetResult result, ThreadUtil threadUtil, IAction action) {
                String key = resultKey();
                String taskKey = EncryptionTool.encrypt(key);
                NetResult.AsyncDelayResult delayResult = new NetResult.AsyncDelayResult(taskKey);
                delayResult.setData(null);
                post(taskKey, delayResult);
                threadUtil.doWithAsync(() -> {
                    return key;
                }, null, new AsyncToDo() {
                    @Override
                    public void doSomething(AsyncTaskResult async) throws Exception {
                        Object data = null;
                        Throwable ex = null;
                        try {
                            data = action.action(result);
                        } catch (Throwable e) {
                            ex = e;
                        }
                        NetResult.AsyncDelayResult delayResult1 = new NetResult.AsyncDelayResult(taskKey);
                        if (ex != null) {
                            delayResult1.setData(ex.getMessage());
                            delayResult1.setStatus(NetResult.AsyncDelayResult.ERROR);
                            log.error("异步延迟请求错误:" + ex.getMessage(), ex);
                        } else {
                            delayResult1.setData(data);
                            delayResult1.setStatus(NetResult.AsyncDelayResult.DONE);
                        }
                        post(taskKey, delayResult1);
                    }
                }).execute();
                result.setMethodType(NetResult.MethodType.AsynchronousDelay);
                result.setData(delayResult);
                return result;
            }

            @Override
            public boolean post(String key, Object o) {
                try {
                    dataSource.put(key, (NetResult.AsyncDelayResult) o);
                    return true;
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                return false;
            }

            @Override
            public Object get(String key) throws Exception {
                if (dataSource.containsKey(key)) {
                    NetResult.AsyncDelayResult data = dataSource.get(key);
                    if(!NetResult.AsyncDelayResult.RUNNING.equals(data.getStatus()))
                        dataSource.remove(key);
                    return data;
                } else {
                    throw new NetError("不存在取值码:" + key, 404);
                }

            }
        };
    }


    @Bean
    @ConditionalOnMissingBean(AsyncTaskExecutor.class)
    @Primary
    AsyncTaskExecutor getExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(16);
        threadPoolTaskExecutor.setMaxPoolSize(128);
        threadPoolTaskExecutor.setQueueCapacity(512);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setThreadNamePrefix("asynctask-");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @Bean
    @ConditionalOnMissingBean(value = IThreadLocker.class)
    IThreadLocker defaultLocker() {
        return new IThreadLocker() {
            Map<String, Object> keys = new HashMap<>();

            @Override
            public boolean isLock(IThreadSign sign) {
                return keys.containsKey(sign.key());
            }

            @Override
            public void lock(IThreadSign sign) {
                if (!isLock(sign)) {
                    keys.put(sign.key(), null);
                }
            }

            @Override
            public void unlock(IThreadSign sign) {
                keys.remove(sign.key());
            }

            @Override
            public <T> void putObject(IThreadSign sign, T obj) {
                if (!isLock(sign)) {
                    return;
                }
                keys.put(sign.key(), obj);
            }

            @Override
            public <T> T getObject(IThreadSign sign, Class<? extends T> clas) {
                if (!isLock(sign)) {
                    return null;
                }
                return (T) keys.get(sign.key());
            }

            @Override
            public <T> List<T> getList(IThreadSign sign, Class<T> clas) {
                if (!isLock(sign)) {
                    return null;
                }
                return (List<T>) keys.get(sign.key());
            }
        };
    }
}
