package work.layman.refresgJwtToken.utils;

import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/4/11.
 */
@ConfigurationProperties("jwt.config")
public class JwtUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private String key;

    private long ttl;//一个小时

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    /**
     * 生成JWT
     *
     * @param id
     * @param subject
     * @return
     */
    public String createJWT(String id, String subject, String role) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder().setId(id)
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, key).claim("role", role);
        if (ttl > 0) {
            builder.setExpiration(new Date(nowMillis + ttl));
        }
        String token = builder.compact();
        // 以token过期的1.5倍时间将token写入缓存, key:token value:id-username-role
        redisTemplate.opsForValue().set(token, id + "-" + subject + "-" + role, (long) ((ttl / 1000) * 1.5), TimeUnit.SECONDS);
        return token;
    }

    /**
     * @return java.lang.String
     * @Author lizhangjun
     * @Description 生成app jwt
     * @Date 2019/5/22 17:46
     * @Param [id, subject, role, ttl]
     **/
    public String createAppJWT(String id, String subject, String role, long ttl) {
        this.ttl = ttl;
        String token = createJWT(id, subject, role);
        return token;
    }

    /**
     * 解析JWT
     *
     * @param jwtStr
     * @return
     */
    public Claims parseJWT(String jwtStr) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwtStr)
                .getBody();
    }

    /**
     * <pre>
     *  验证token是否失效
     *  true:过期   false:没过期
     * </pre>
     */
    public Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException expiredJwtException) {
            return true;
        }
    }

    /**
     * 获取jwt失效时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token).getExpiration();
    }

    /**
     * 获取jwt的payload部分
     */
    public Claims getClaimFromToken(String token) {
        return Jwts.parser()   //得到DefaultJwtParser
                .setSigningKey(key)  //设置签名的秘钥
                .parseClaimsJws(token)
                .getBody();
    }

    public String refreshToken(String token) {
        try {
            String redisUserInfo = redisTemplate.opsForValue().get(token);
            if (StringUtils.isNotBlank(redisUserInfo)) {
                redisTemplate.delete(token);
                String[] infos = redisUserInfo.split("-");
                String userId = infos[0];
                String username = infos[1];
                String role = infos[2];
                String newToken = this.createJWT(userId, username, role);
                return newToken;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
