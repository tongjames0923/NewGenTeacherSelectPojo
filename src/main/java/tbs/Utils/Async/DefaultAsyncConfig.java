package tbs.Utils.Async;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import tbs.Utils.Async.interfaces.IThreadLocker;
import tbs.Utils.Async.interfaces.IThreadSign;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class DefaultAsyncConfig {



    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean(IThreadSign.class)
    IThreadSign defaultSign()
    {
        return new IThreadSign() {

            String key="asyncTaskSign_"+UUID.randomUUID().toString();
            @Override
            public String key() {
                return key;
            }
        };
    }


    @Bean
    @ConditionalOnMissingBean(AsyncTaskExecutor.class)
    AsyncTaskExecutor getExecutor()
    {
        ThreadPoolTaskExecutor threadPoolTaskExecutor=new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(128);
        threadPoolTaskExecutor.setMaxPoolSize(512);
        threadPoolTaskExecutor.setQueueCapacity(1024);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @Bean
    @ConditionalOnMissingBean(value = IThreadLocker.class)
    IThreadLocker defaultLocker()
    {
        return new IThreadLocker() {
            Map<String,Object> keys=new HashMap<>();

            @Override
            public boolean isLock(IThreadSign sign) {
                return keys.containsKey(sign.key());
            }

            @Override
            public void lock(IThreadSign sign) {
                if(!isLock(sign))
                    keys.put(sign.key(),null);
            }

            @Override
            public void unlock(IThreadSign sign) {
                if(keys.containsKey(sign.key()))
                    keys.remove(sign.key());
            }

            @Override
            public <T> void putObject(IThreadSign sign, T obj) {
                if(!isLock(sign))
                    return;
                keys.put(sign.key(),obj);
            }

            @Override
            public <T> T getObject(IThreadSign sign,Class<? extends T> clas) {
                if(!isLock(sign))
                    return null;
                return (T) keys.get(sign.key());
            }

            @Override
            public <T> List<T> getList(IThreadSign sign, Class<T> clas) {
                if(!isLock(sign))
                    return null;
                return (List< T>) keys.get(sign.key());
            }
        };
    }
}
