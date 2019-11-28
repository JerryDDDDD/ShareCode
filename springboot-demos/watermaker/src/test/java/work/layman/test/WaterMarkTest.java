package work.layman.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import work.layman.WatermakerApplication;
import work.layman.service.WaterMakerService;

/**
 * @ClassName WaterMarkTest
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/10/24 11:36
 * @Version 3.0
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WatermakerApplication.class)
public class WaterMarkTest {

    @Autowired
    WaterMakerService waterMakerService;

    @Test
    public void addWaterMaker() throws Exception {
        waterMakerService.addWaterMark();
    }
}
