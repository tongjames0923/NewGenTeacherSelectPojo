package tbs.utils.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private Integer port;
    @Value("${spring.redis.timeout}")
    private String timeout;
    @Value("${spring.redis.password}")
    private String password;

    public static final String LongTermCache = "LONG_TERM_CACHE",ShortTermCache="SHORT_TERM_CACHE";

    /**
     * 缓存管理器
     */
    @Bean(LongTermCache)
    @Primary
    public RedisCacheManager myCacheManager3(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(6))   // 设置缓存过期时间
                .disableCachingNullValues()     // 禁用缓存空值，不缓存null校验
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new
                        GenericJackson2JsonRedisSerializer()));     // 设置CacheManager的值序列化方式为json序列化，可加入@Class属性
        // 使用RedisCacheConfiguration创建RedisCacheManager
        RedisCacheManager cm = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfiguration).build();
        return cm;
    }

    @Bean(ShortTermCache)
    public RedisCacheManager sortTerm(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))   // 设置缓存过期时间
                .disableCachingNullValues()     // 禁用缓存空值，不缓存null校验
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new
                        GenericJackson2JsonRedisSerializer()));     // 设置CacheManager的值序列化方式为json序列化，可加入@Class属性
        // 使用RedisCacheConfiguration创建RedisCacheManager
        RedisCacheManager cm = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfiguration).build();
        return cm;
    }


//    @Bean
//    public RedisConnectionFactory redisConnectionFactory()
//    {
//        return connectionFactory(host,port,password);
//    }
//
//
//
//
//    public RedisConnectionFactory connectionFactory(String host, int port, String password) {
//        LettuceConnectionFactory connectionFactory=new LettuceConnectionFactory();
//        connectionFactory.afterPropertiesSet();
//        return connectionFactory;
//    }
}
