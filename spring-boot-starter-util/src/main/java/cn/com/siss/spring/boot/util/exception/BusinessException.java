package cn.com.siss.spring.boot.util.exception;

/**
 * @Description use the standard {@link /gitlab.sissyun.com.cn/frameworks/siss-common} constraint instead
 */
@Deprecated
public class BusinessException extends RuntimeException{

    public BusinessException(String message) {
        super(message);
    }

}
