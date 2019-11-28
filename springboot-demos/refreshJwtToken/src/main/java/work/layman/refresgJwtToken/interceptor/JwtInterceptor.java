package work.layman.refresgJwtToken.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import work.layman.refresgJwtToken.utils.CookieUtils;
import work.layman.refresgJwtToken.utils.JsonWebTokenUtil;
import work.layman.refresgJwtToken.utils.JwtUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ClassName JwtIntercepter
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/10/25 11:39
 * @Version 3.0
 **/
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JsonWebTokenUtil jsonWebTokenUtil;

    @Autowired
    private HttpSession session;

    private static Logger logger = LoggerFactory.getLogger(JwtInterceptor.class.getName());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("[cpw]enter JwtInterceptor......");
        System.out.println(request.getCookies());
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("Cookie ====>" + cookie.getName() + ":" + cookie.getValue());
                // 如果cookie的名称是Admin登录的cookie
                if (cookie.getName().equals("USER")) {
                    String token = cookie.getValue();
                    // 验证令牌
                    //判断过期token是否合法
                    Boolean tokenExpired = jwtUtil.isTokenExpired(token);
                    System.out.println("is token Expired" + tokenExpired);
                    if (tokenExpired) {
                        // token 过期获取验证获取新的token
                        String newToken = jwtUtil.refreshToken(token);
                        // newToken不为空, 新的token签发成功,否则无新的token
                        if (newToken != null) {
                            CookieUtils.setCookie(request, response, "USER", newToken);
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
