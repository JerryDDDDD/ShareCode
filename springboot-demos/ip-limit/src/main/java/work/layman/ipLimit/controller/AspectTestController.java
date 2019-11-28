package work.layman.ipLimit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import work.layman.ipLimit.aop.IPLimit;

import java.util.logging.Logger;

/**
 * @ClassName AspectTestController
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/11/28 15:52
 * @Version 3.0
 **/
@Controller
public class AspectTestController {

    private Logger logger = Logger.getLogger(AspectTestController.class.getName());

    @IPLimit()
    @RequestMapping("/aspect")
    public String aspectTest() {
        logger.info("请求成功");
        return "ok";
    }
}
