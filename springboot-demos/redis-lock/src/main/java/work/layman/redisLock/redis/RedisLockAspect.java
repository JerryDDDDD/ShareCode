package work.layman.redisLock.redis;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @ClassName RedisUtil
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/11/23 15:33
 * @Version 3.0
 **/
@Aspect
@Component
public class RedisLockAspect {

    private static final Integer MAX_RETRY_COUNT = 3;

    // 锁前缀
    public static final String LOCK_PREFIX = "redis_lock";

    // 锁过期时间
    public static final int LOCK_EXPIRE = 300;

    private static final String LOCK_KEY = "lockKey";
    private static final String TIME_OUT = "timeOut"; // second
    private static final int PROTECT_TIME = 2 << 11;//4096

    private static final Logger logger = Logger.getLogger(RedisLockAspect.class.getName());

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Pointcut("@annotation(work.layman.redisLock.redis.RedisLock)")
    public void redisLockAspect() {
    }

    @Before("redisLockAspect()")
    public void doBefore(JoinPoint proceedingJoinPoint) {
        try {
            boolean lockFlag = this.getLock(proceedingJoinPoint, 0, System.currentTimeMillis());
            if (lockFlag) {
                logger.info("this thread got the lock");
            }
        } catch (Throwable throwable) {
            throw new RuntimeException("get lock exception" + throwable.getMessage());
        } finally {
            logger.info("release lock");

        }
    }


    @After("redisLockAspect()")
    public void doAfter(JoinPoint proceedingJoinPoint) {
        System.out.println("after");
        delLock(proceedingJoinPoint);
    }

    /**
     * @return
     * @Author 叶泽文
     * @Description 获取锁
     * @Date 16:59 2019/11/23
     * @Param
     **/
    private boolean getLock(JoinPoint proceedingJoinPoint, int count, long currentTime) {
        // 获取注解中的参数
        Map<String, Object> annotationArgs = this.getAnnotationArgs(proceedingJoinPoint);
        String lockPrefix = (String) annotationArgs.get(LOCK_PREFIX);
        String key = (String) annotationArgs.get(LOCK_KEY);
        long expire = (long) annotationArgs.get(TIME_OUT);
        if (StringUtils.isEmpty(lockPrefix) || StringUtils.isEmpty(key)) {
            // 此条执行不到
            throw new RuntimeException("RedisLock,锁前缀,锁名未设置");
        }
        if (redisUtil.setNx(lockPrefix, key, expire)) {
            return true;
        } else {
            // 拿不到锁
            // 如果创建时间与当前时间大于保护时间 则强制删除锁
            long createTime = redisUtil.getLocalValue(lockPrefix, key);
            if ((currentTime - createTime) > (expire * 1000 + PROTECT_TIME)) {
                count++;
                if (count > MAX_RETRY_COUNT) {
                    return false;
                }
                redisUtil.delete(lockPrefix, key);
                getLock(proceedingJoinPoint, count, currentTime);
            }
            return false;
        }
    }


    /**
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Author 叶泽文
     * @Description 获取注解内容
     * @Date 17:47 2019/11/23
     * @Param [proceedingJoinPoint]
     **/
    private Map<String, Object> getAnnotationArgs(JoinPoint proceedingJoinPoint) {
        Class target = proceedingJoinPoint.getTarget().getClass();
        Method[] methods = target.getMethods();
        String methodName = proceedingJoinPoint.getSignature().getName();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Map<String, Object> result = new HashMap<>();
                RedisLock redisLock = method.getAnnotation(RedisLock.class);
                result.put(LOCK_PREFIX, redisLock.lockPrefix());
                result.put(LOCK_KEY, redisLock.lockKey());
                result.put(TIME_OUT, redisLock.timeUnit().toSeconds(redisLock.timeOut()));
                return result;
            }
        }
        return null;
    }


    /**
     * @return boolean
     * @Author 叶泽文
     * @Description 删除锁
     * @Date 17:47 2019/11/23
     * @Param [proceeding]
     **/
    private boolean delLock(JoinPoint proceeding) {
        Map<String, Object> annotationArgs = this.getAnnotationArgs(proceeding);
        String lockPrefix = (String) annotationArgs.get(LOCK_PREFIX);
        String key = (String) annotationArgs.get(LOCK_KEY);
        return redisUtil.delete(lockPrefix, key);
    }
}
