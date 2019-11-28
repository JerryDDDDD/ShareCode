package work.layman.refresgJwtToken.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import work.layman.refresgJwtToken.interceptor.JwtInterceptor;
import work.layman.refresgJwtToken.interceptor.NewJwtInterceptor;

/**
 * @ClassName InterceptorConfig
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/10/25 14:51
 * @Version 3.0
 **/
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Autowired
//    private NewJwtInterceptor jwtInterceptor;
    private JwtInterceptor jwtInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器要声明拦截器对象和要拦截的请求
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**/*.json").excludePathPatterns("/login/login.do");
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**/*.html");
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }
}
