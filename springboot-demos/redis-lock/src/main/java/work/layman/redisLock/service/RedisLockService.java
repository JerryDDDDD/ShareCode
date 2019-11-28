package work.layman.redisLock.service;

import org.springframework.stereotype.Service;
import work.layman.redisLock.redis.RedisLock;

/**
 * @ClassName RedisLockService
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/11/23 17:53
 * @Version 3.0
 **/
@Service
public class RedisLockService {

    @RedisLock(lockKey = "lock", lockPrefix = "redis", timeOut = 1)
    public String redisLock() {
        System.out.println("Redis Layman");
        return "OK";
    }
}
