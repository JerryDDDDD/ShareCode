package work.layman.ipLimit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @ClassName DemoController
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/11/28 10:19
 * @Version 3.0
 **/
@Controller
public class DemoController {

    private static final Logger logger = Logger.getLogger(DemoController.class.getName());

    private static final String prefix = "Demo";

    /**
     * key过期时间（秒）
     */
    private final Long EXPIRATIONTIME_SECONDS = 60L;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("/login")
    public String login(HttpServletRequest request) {
        // 获取Http请求ip
        String ip = request.getHeader("x-forwarded-for");
        logger.info("remote ip is :" + ip);
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
            if (Integer.valueOf(sumVal) > 3) {
                logger.warning("第" + sumVal + "次请求,请求失败");
                return "一分钟内不能在次请求!";
            }
        } else {
            redisTemplate.opsForValue().set(key, "1", EXPIRATIONTIME_SECONDS, TimeUnit.SECONDS);
        }

        String num = redisTemplate.opsForValue().get(key);
        logger.info("第" + num + "次请求,请求成功");
        return "请求成功";
    }
}
