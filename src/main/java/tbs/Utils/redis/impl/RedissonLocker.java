package tbs.utils.redis.impl;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import tbs.utils.Async.interfaces.ILocker;
import tbs.utils.Async.interfaces.IThreadLocker;
import tbs.utils.Async.interfaces.IThreadSign;
import tbs.utils.redis.RedisConfig;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component(RedisConfig.REDISSION_LOCK)
public class RedissonLocker implements ILocker {

    @Resource
    RedissonClient client;

    @Value("${tbs.thread.locker.redis.max_lock_timeout:120}")
    long max_timeout = 120;

    @Override
    public boolean isLock(IThreadSign sign) {
        RLock lock = client.getLock(sign.key());
        return lock.isLocked();
    }

    @Override
    public void lock(IThreadSign sign) {
        RLock lock = client.getLock(sign.key());
        lock.lock(max_timeout, TimeUnit.SECONDS);
    }

    @Override
    public void unlock(IThreadSign sign) {
        RLock lock = client.getLock(sign.key());
        if (lock.isLocked())
            lock.unlock();
    }
}
