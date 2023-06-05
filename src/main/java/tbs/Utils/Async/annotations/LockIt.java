package tbs.utils.Async.annotations;

import tbs.utils.redis.RedisConfig;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockIt {
    String value()default "";
    String lockType() default RedisConfig.REDISSION_LOCK;
}
