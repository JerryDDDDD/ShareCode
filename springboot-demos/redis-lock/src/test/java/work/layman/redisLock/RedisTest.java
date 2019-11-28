package work.layman.redisLock;

import org.aspectj.lang.annotation.Around;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import work.layman.redisLock.service.RedisLockService;

/**
 * @ClassName RedisTest
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/11/23 17:48
 * @Version 3.0
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedisLockApplication.class)
public class RedisTest {

    @Autowired
    private RedisLockService redisLockService;

    @Test
    public void test() {
        redisLockService.redisLock();
    }
}
