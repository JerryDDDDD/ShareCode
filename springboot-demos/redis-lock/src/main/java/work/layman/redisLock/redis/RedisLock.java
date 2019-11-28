package work.layman.redisLock.redis;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisLock
 * @Description TODO redis同步锁注解
 * @Author lizhangjun
 * @Data 2019/5/30 14:08
 * @Version 3.0.0-beta.1
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedisLock
{
    // 锁前缀
    String lockPrefix() default "";

    // 锁名
    String lockKey() default "";

    // 锁持续时间
    long timeOut() default 5;

    // 时间单位
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}