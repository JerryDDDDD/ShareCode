package work.layman.ipLimit.aop;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IPLimit {
    int MAXTIME() default 10;

    int TIMEOUT() default 60;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
