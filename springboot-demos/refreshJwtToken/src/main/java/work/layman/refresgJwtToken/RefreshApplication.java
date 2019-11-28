package work.layman.refresgJwtToken;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import work.layman.refresgJwtToken.utils.JwtUtil;

/**
 * @ClassName RefeshApplication
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/10/25 10:10
 * @Version 3.0
 **/
@SpringBootApplication
public class RefreshApplication {

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(RefreshApplication.class, args);
    }
}
