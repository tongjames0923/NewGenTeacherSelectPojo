package tbs.utils.redis;


import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author abstergo
 */
public interface IRedisService {
    <T> T get(String key,Class<T> tClass);

    <T> List<T> getList(String key, Class<T> tClass);

    <T> void set(String key, T data, long timeout, TimeUnit unit);
    <T> void set(String key, T data);
    void delete(String key);
    void expire(String key,long timeout,TimeUnit unit);

    long getExpire(String key,TimeUnit unit);

    boolean hasKey(String key);
}
