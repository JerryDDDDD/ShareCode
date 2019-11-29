package work.layman.nettyIPLimit.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IPLimit {
    int MAXTIME() default 10;

    int TIMEOUT() default 60;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
