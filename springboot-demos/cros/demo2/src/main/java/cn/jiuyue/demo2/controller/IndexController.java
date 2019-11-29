package cn.jiuyue.demo2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Create bySeptember
 * 2019/8/10
 * 12:04
 */
@Controller
public class IndexController {
    @ResponseBody
    @RequestMapping("/index")
    public String index(){
        return "index";
    }
}
