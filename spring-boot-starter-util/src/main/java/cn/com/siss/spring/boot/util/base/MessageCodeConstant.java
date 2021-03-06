package cn.com.siss.spring.boot.util.base;

import java.io.Serializable;

/**
 * @ClassName MessageCodeConstant
 * @Description use the standard {@link /gitlab.sissyun.com.cn/frameworks/siss-common} constraint instead
 * @Author clare
 * @Date 2019/2/19 11:25
 * @Version 1.0
 */
@Deprecated
public class MessageCodeConstant implements Serializable {
    /**
     * 成功
     */
    public static final String MESSAGE_COMMON_SUCCESS = "message.common.success";

    /**
     * 失败
     */
    public static final String MESSAGE_COMMON_FAILED = "message.common.failed";

    /**
     * 信息不存在
     */
    public static final String MESSAGE_COMMON_INFORMATION_NONEXISTENCE = "message.common.information.nonexistence";

    /**
     * 支付方式错误
     */
    public static final String MESSAGE_PAYMENT_TYPE_ERROR = "message.payment.type.error";

    /**
     * 服务器处理异常
     */
    public static final String MESSAGE_COMMON_SERVER_ERROR = "message.common.server.error";

    /**
     * 参数不全
     */
    public static final String MESSAGE_COMMON_PARAMETERS_MISSING = "message.common.parameters.missing";

    /**
     * 请求超时
     */
    public static final String MESSAGE_COMMON_TIMEOUT = "message.common.timeout";

    /**
     * 信息已存在
     */
    public static final String MESSAGE_COMMON_INFORMATION_ALREADYEXISTS = "message.common.information.already.exists";

    /**
     * 登录状态失效
     */
    public static final String MESSAGE_COMMON_SESSION_EXPIRED = "message.common.session.expired";

    /**
     * 验证失败
     */
    public static final String MESSAGE_SECURITY_AUTH_FAILED = "message.security.auth.failed";

    /**
     * 无权限访问
     */
    public static final String MESSAGE_SECURITY_UNAUTHORIZED = "message.security.unauthorized";

    /**
     * 插入失败
     */
    public static final String MESSAGE_COMMON_INSERT_FAILED = "message.common.insert.failed";

    /**
     * 删除失败
     */
    public static final String MESSAGE_COMMON_DELETE_FAILED = "message.common.delete.failed";

    public static final String MESSAGE_NULL_POINTER_EXCEPTION = "message.null.pointer.exception";

    public static final String MESSAGE_RUNTIME_EXCEPTION = "message.runtime.exception";

    public static final String MESSAGE_EXCEPTION = "message.exception";

}
