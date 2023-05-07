package tbs.utils.redis.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import tbs.utils.redis.IRedisService;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author abstergo
 */
@Component
@Slf4j
public class DefaultRedisServiceImpl implements IRedisService {

    @Resource
    RedisTemplate<String, String> template;

    @Override
    public <T> T get(String key, Class<T> tClass) {
        try {
            String json = template.opsForValue().get(key);
            return JSON.parseObject(json, tClass);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return null;
        }
    }

    @Override
    public <T> List<T> getList(String key, Class<T> tClass) {
        try {
            String json = template.opsForValue().get(key);
            return JSON.parseArray(json, tClass);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return null;
        }
    }

    @Override
    public <T> void set(String key, T data, long timeout, TimeUnit unit) {
        String text = JSON.toJSONString(data);
        template.opsForValue().set(key, text, timeout, unit);
    }

    @Override
    public <T> void set(String key, T data) {
        String text = JSON.toJSONString(data);
        template.opsForValue().set(key, text);
    }

    @Override
    public void delete(String key) {
        template.delete(key);
    }

    @Override
    public void expire(String key, long timeout, TimeUnit unit) {
        template.expire(key, timeout, unit);
    }

    @Override
    public long getExpire(String key, TimeUnit unit) {
        return template.getExpire(key, unit);
    }


    @Override
    public boolean hasKey(String key) {
        return template.hasKey(key);
    }

}
