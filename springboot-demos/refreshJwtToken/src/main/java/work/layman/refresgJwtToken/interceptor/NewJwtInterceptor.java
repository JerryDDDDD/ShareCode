package work.layman.refresgJwtToken.interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import work.layman.refresgJwtToken.utils.JsonWebTokenUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName NewJwtInterceptor
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/10/26 14:58
 * @Version 3.0
 **/
@Component
public class NewJwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JsonWebTokenUtil jsonWebTokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("USER")) {
                String token = cookie.getValue();
                String userId = jsonWebTokenUtil.checkExpireToken(token);
                jsonWebTokenUtil.getGoodToken(userId);
                return true;
            }
        }
        return false;
    }
}
