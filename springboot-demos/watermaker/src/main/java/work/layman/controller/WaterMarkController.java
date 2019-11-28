package work.layman.controller;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName WaterMarkController
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/10/24 11:35
 * @Version 3.0
 **/
@RequestMapping("water")
public class WaterMarkController {

    @RequestMapping("/mark.json")
    public String addWaterMark() {

        return "ok";
    }
}
