package work.layman.refresgJwtToken.entity;
/**
 *@ClassName StatusCode
 *@Description TODO 状态码实体类
 *@Author 李章俊
 *@Data 2019/4/16 16:40
 *@Version 3.0
 **/
public class StatusCode
{
    public static final int OK=20000;//成功
    public static final int ERROR =20001;//失败
    public static final int LOGINERROR =20002;//密码错误
    public static final int ACCESSERROR =20003;//权限不足
    public static final int REMOTEERROR =20004;//远程调用失败
    public static final int REPERROR =20005;//重复操作
    public static final int EXPIRES =20006;//令牌失效
    public static final int TIMEERROR =20007;//时间格式出错
    public static final int NOUSER= 20008;// 查无用户-用户未注册
    public static final int PHONEUNREGISTER = 20009; //手机号未注册
    public static final int PHONEREGISTERED = 20010; //手机号已注册
    public static final int COMPANYNUMERROR = 20011; //企业识别号错误
}

