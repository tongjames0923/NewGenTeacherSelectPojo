package tbs.Utils.Async;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class DefaultAsyncConfig {

    @Bean
    @ConditionalOnMissingBean(AsyncTaskExecutor.class)
    AsyncTaskExecutor getExecutor()
    {
        ThreadPoolTaskExecutor threadPoolTaskExecutor=new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(16);
        threadPoolTaskExecutor.setMaxPoolSize(128);
        threadPoolTaskExecutor.setQueueCapacity(256);
        threadPoolTaskExecutor.initialize();
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolTaskExecutor;
    }

    @Bean
    @ConditionalOnMissingBean(value = IThreadLocker.class)
    IThreadLocker defaultLocker()
    {
        return new IThreadLocker() {
            HashMap<String,Boolean> keys=new HashMap<>();

            @Override
            public boolean isLock(IThreadSign sign) {
                return keys.getOrDefault(sign.key(),false);
            }

            @Override
            public void lock(IThreadSign sign) {
                if(!isLock(sign))
                    keys.put(sign.key(),true);
            }

            @Override
            public void unlock(IThreadSign sign) {
                if(keys.containsKey(sign.key()))
                    keys.remove(sign.key());
            }
        };
    }
}
