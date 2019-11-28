package work.layman.refresgJwtToken.utils;

import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName JsonWebTokenUtil
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/10/26 10:37
 * @Version 3.0
 **/
@Component
public class JsonWebTokenUtil {

    private static final long ADVANCE_EXPIRE_TIME =   1800000;
    private static final long JWT_EXPIRE_TIME_LONG =  7000000;
    private static final long OLD_TOKEN_EXPIRE_TIME = 3600000;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取用户从token中
     */
    public String getUserFromToken(String token) {
        return getClaimFromToken(token).getSubject();
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
     * 获取可用的token
     * 如该用户当前token可用，即返回
     * 当前token不可用，则返回一个新token
     * @param userId
     * @return
     */
    public String getGoodToken(String userId){
        String token = redisTemplate.opsForValue().get("userJwtToken_"+userId);
        boolean flag = this.checkToken(token);
        //校验当前token能否使用，不能使用则生成新token
        // false 需要重新生成 true不需要
        if(flag){
            return token;
        }else{
            String newToken = this.createToken(userId);
            //初始化新token
            this.initNewToken(userId, newToken);
            return newToken;
        }
    }

    /**
     * 判断过期token是否合法
     * @param token
     * @return
     */
    public String checkExpireToken(String token){
        //判断token是否需要更新
        // false 需要更新 true 不需要更新
        boolean expireFlag = this.checkToken(token);
        //false：不建议使用
        if(!expireFlag){
            String userId = redisTemplate.opsForValue().get(token);
            if(StringUtils.isNotBlank(userId)){
                return userId + "-1";
            }
        }else{
            String userId = this.getUserFromToken(token);
            return userId;
        }
        return "";
    }

    /**
     * 检查当前token是否还能继续使用
     * true：可以  false：不建议
     * @param token
     * @return
     */
    public boolean checkToken(String token){
        SecretKey secretKey = this.createSecretKey();
        try {
            // jwt正常情况 则判断失效时间是否大于5分钟
            long expireTime = Jwts.parser()   //得到DefaultJwtParser
                    .setSigningKey(secretKey)  //设置签名的秘钥
                    .parseClaimsJws(token.replace("jwt_", ""))
                    .getBody().getExpiration().getTime();
            long diff = expireTime - System.currentTimeMillis();
            //如果有效期小于5分钟，则不建议继续使用该token
//            if (diff < ADVANCE_EXPIRE_TIME) {
//                return false;
//            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 创建新token
     * @param userId 用户ID
     * @return
     */
    public String createToken(String userId){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        long nowMillis = System.currentTimeMillis();//生成JWT的时间
        Date now = new Date(nowMillis);
//        Map<String,Object> claims = new HashMap<String,Object>();
        // 创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        //生成签名的时候使用的秘钥secret,这个方法本地封装了的，一般可以从本地配置文件中读取，切记这个秘钥不能外露哦。
        // 它就是你服务端的私钥，在任何场景都不应该流露出去。一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
        SecretKey secretKey = createSecretKey();
        //下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder() //这里其实就是new一个JwtBuilder，设置jwt的body
//                .setClaims(claims)          //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setId(UUID.randomUUID().toString())                  //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)           //iat: jwt的签发时间
                .setSubject(userId + "-" + "1.0.0")        //sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .signWith(signatureAlgorithm, secretKey);//设置签名使用的签名算法和签名使用的秘钥
        //设置过期时间
        if (JWT_EXPIRE_TIME_LONG >= 0) {
            long expMillis = nowMillis + JWT_EXPIRE_TIME_LONG;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        String newToken = "jwt_" + builder.compact();
        return newToken;
    }

    /**
     * 生成新token时，初始化token
     * @param userId
     * @param newToken
     */
    public void initNewToken(String userId, String newToken){
        String token = redisTemplate.opsForValue().get("userJwtToken_"+userId);
        if(StringUtils.isNotBlank(token)){
            //老token设置过期时间 5分钟
            redisTemplate.opsForValue().set(token, userId, OLD_TOKEN_EXPIRE_TIME, TimeUnit.MINUTES);
        }
        //新token初始化
        redisTemplate.opsForValue().set(newToken, userId);
        redisTemplate.opsForValue().set("userJwtToken_"+userId, newToken);
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
        SecretKey secretKey = createSecretKey();
        return Jwts.parser()   //得到DefaultJwtParser
                .setSigningKey(secretKey)  //设置签名的秘钥
                .parseClaimsJws(token.replace("jwt_", ""))
                .getBody();
    }

    // 签名私钥
    private SecretKey createSecretKey(){
        byte[] encodedKey = Base64.decodeBase64("123456");//本地的密码解码
        SecretKey secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");// 根据给定的字节数组使用AES加密算法构造一个密钥，使用 encodedKey中的始于且包含 0 到前 leng 个字节这是当然是所有。（后面的文章中马上回推出讲解Java加密和解密的一些算法）
        return secretKey;
    }
}
