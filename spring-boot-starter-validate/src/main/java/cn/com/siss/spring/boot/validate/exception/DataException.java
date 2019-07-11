package cn.com.siss.spring.boot.validate.exception;

/**
 * @ClassName DataException
 * @Description use the standard {@link /gitlab.sissyun.com.cn/frameworks/siss-common} constraint instead
 * @Author clare
 * @Date 2019/2/20 15:19
 * @Version 1.0
 */
@Deprecated
public class DataException extends Exception {

    public DataException(String msg) {
        super(msg);
    }
}
