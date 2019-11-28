package work.layman.ipLimit.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @ClassName IPLimitAspect
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/11/28 11:55
 * @Version 3.0
 **/
@Aspect
@Component
public class IPLimitAspect {

    private static final Logger logger = Logger.getLogger(IPLimitAspect.class.getName());

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String MAXTIME = "max-time";

    private static final String TIME_OUT = "time-out";

    private static final String TIMEUNIT = "time-unit";

    private static final String prefix = "Demo";

    /**
     * key过期时间（秒）
     */
    private final Long EXPIRATIONTIME_SECONDS = 60L;

    @Pointcut("@annotation(work.layman.ipLimit.aop.IPLimit)")
    public void IPLimitAspect(){
    }

    @Before("IPLimitAspect()")
    public void doBefore(JoinPoint joinPoint) {
        Map<String, Object> annotationArgs = getAnnotationArgs(joinPoint);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String remoteHost = request.getRemoteHost();
        logger.info("远端地址是:" + remoteHost);
        Boolean stop = isStop(request, Integer.valueOf(annotationArgs.get(MAXTIME).toString()), Long.valueOf(annotationArgs.get(TIME_OUT).toString()),
                TimeUnit.valueOf(annotationArgs.get(TIMEUNIT).toString()));
        if (stop) {
            throw new IPLimitException("访问限制");
        }
    }


    private Map<String, Object> getAnnotationArgs(JoinPoint proceedingJoinPoint) {
        Class target = proceedingJoinPoint.getTarget().getClass();
        Method[] methods = target.getMethods();
        String methodName = proceedingJoinPoint.getSignature().getName();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Map<String, Object> result = new HashMap<>();
                IPLimit ipLimit = method.getAnnotation(IPLimit.class);
                result.put(MAXTIME, ipLimit.MAXTIME());
                result.put(TIME_OUT, ipLimit.TIMEOUT());
                result.put(TIMEUNIT, ipLimit.timeUnit());
                return result;
            }
        }
        return null;
    }

    private Boolean isStop(HttpServletRequest request, Integer maxTime, Long expireTime, TimeUnit timeUnit) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "null".equals(ip)) {
            ip = "" + request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "null".equals(ip)) {
            ip = "" + request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "null".equals(ip)) {
            ip = "" + request.getRemoteAddr();
        }

        // redis key
        String key = prefix + ip;

        boolean flag = redisTemplate.hasKey(key);
        if (flag) {
            redisTemplate.boundValueOps(key).increment(1);
            redisTemplate.expire(key, EXPIRATIONTIME_SECONDS, TimeUnit.SECONDS);
            String sumVal = redisTemplate.opsForValue().get(key);
            if (Integer.valueOf(sumVal) > maxTime) {
                logger.warning("第" + sumVal + "次请求,请求失败");
                return true;
            }
        } else {
            redisTemplate.opsForValue().set(key, "1", expireTime, timeUnit);
        }
        String num = redisTemplate.opsForValue().get(key);
        logger.info("第" + num + "次请求,请求成功");
        return false;
    }
}
