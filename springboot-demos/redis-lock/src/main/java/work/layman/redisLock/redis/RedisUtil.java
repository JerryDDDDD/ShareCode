package work.layman.redisLock.redis;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisUtil
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/11/23 17:13
 * @Version 3.0
 **/
@Component
public class RedisUtil {

    //同步锁前缀
    public static final String LOCK_PREFIX = "redis_lock";

    //锁默认过期时间（单位ms）
    public static final int LOCK_EXPIRE = 300;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * @Author 叶泽文
     * @Description 加锁
     *         SetNx keyName value 如果key不存在设置增加key设置值返回1, 如果key存在返回0
     * @Date 17:13 2019/11/23
     * @Param
     * @return
     **/
    public boolean setNx(String prefix, String keyName, long timeOut) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Boolean flag = valueOperations.setIfAbsent(prefix + keyName, String.valueOf(System.currentTimeMillis()), timeOut, TimeUnit.SECONDS);
        return flag;
    }

    /**
     * @Author 叶泽文
     * @Description 删除锁
     * @Date 17:17 2019/11/23
     * @Param [prefix, keyName]
     * @return boolean
     **/
    public boolean delete(String prefix, String keyName) {
        return redisTemplate.delete(prefix + keyName);
    }

    /**    
     * @Author 叶泽文
     * @Description 获取锁的内容 为创建时间
     * @Date 17:19 2019/11/23
     * @Param [prefix, keyName]
     * @return long
     **/
    public long getLocalValue(String prefix, String keyName) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        long createTime = Long.parseLong((String) valueOperations.get(prefix + keyName));
        return createTime;
    }
}
