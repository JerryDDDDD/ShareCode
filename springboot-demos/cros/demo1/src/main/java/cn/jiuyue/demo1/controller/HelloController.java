package cn.jiuyue.demo1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Create bySeptember
 * 2019/8/10
 * 11:56
 */
@Controller
public class HelloController {
    @ResponseBody
    @RequestMapping("/hello")
//    @CrossOrigin(origins = "http://localhost:8082")
    public String  hello(){
        return "hello";
    }

    @ResponseBody
    @RequestMapping("/doPut")
    public String  doPut(){
        return "doPut";
    }
}
