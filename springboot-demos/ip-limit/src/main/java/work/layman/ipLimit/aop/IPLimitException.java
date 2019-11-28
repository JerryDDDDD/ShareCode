package work.layman.ipLimit.aop;

/**
 * @ClassName IPLimitException
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/11/28 16:30
 * @Version 3.0
 **/
public class IPLimitException extends RuntimeException {
    public IPLimitException(String message) {
        super(message);
    }
}
