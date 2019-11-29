package work.layman.nettyIPLimit.netty;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @ClassName NettyBoot
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/11/29 10:41
 * @Version 3.0
 **/
@Component
public class NettyBoot implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // 这个地方会被执行三次
        // 1. FeginContext-base_Service
        // 2. FeginContext-base_Business
        // 3. AnnotationConfigServletWebServerApplicationContext
        if (contextRefreshedEvent.getApplicationContext() instanceof AnnotationConfigServletWebServerApplicationContext) {
            NettyServer.getInstance().start();
        }
    }
}
