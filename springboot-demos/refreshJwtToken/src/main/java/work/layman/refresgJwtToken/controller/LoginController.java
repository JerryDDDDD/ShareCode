package work.layman.refresgJwtToken.controller;


import com.fasterxml.jackson.core.JsonEncoding;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import work.layman.refresgJwtToken.utils.CookieUtils;
import work.layman.refresgJwtToken.utils.JsonWebTokenUtil;
import work.layman.refresgJwtToken.utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName LoginController
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/10/25 10:13
 * @Version 3.0
 **/
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JsonWebTokenUtil jsonWebTokenUtil;

    @ResponseBody
    @RequestMapping("/login.do")
    public String doLogin(HttpServletRequest request, HttpServletResponse response) {
        // 时间以秒计算,token有效刷新时间是token有效过期时间的2倍
        String jwt = jwtUtil.createJWT("1", "123", "1");
//        String jwt = jsonWebTokenUtil.getGoodToken("123");
        CookieUtils.setCookie(request, response,"USER", jwt);
        return "ok";
    }
}
