package cn.com.siss.spring.boot.logging;

import cn.com.siss.framework.common.constant.LogConstant;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Order
@Configuration
public class LogAspect {

    /**
     * 定义RpcService切点Pointcut
     * 第一个*号：表示返回类型， *号表示所有的类型
     * 第二个*号：表示类名，*号表示所有的类
     * 第三个*号：表示方法名，*号表示所有的方法
     * 后面括弧里面表示方法的参数，两个句点表示任何参数
     */
    @Pointcut("execution(* cn.com.siss..*.*RpcService.*(..))")
    public void executionService() {

    }

    /**
     * 定义Controller切点Pointcut
     * 第一个*号：表示返回类型， *号表示所有的类型
     * 第二个*号：表示类名，*号表示所有的类
     * 第三个*号：表示方法名，*号表示所有的方法
     * 后面括弧里面表示方法的参数，两个句点表示任何参数
     */
    @Pointcut("execution(* cn.com.siss..*.*Controller.*(..))")
    public void executionController() {

    }

    /**
     * 定义Async方法切点Pointcut
     */
    @Pointcut("@annotation(org.springframework.scheduling.annotation.Async)")
    public void executionAsync() {

    }

    /**
     * Service方法前置通知,调用之前调用
     *
     * @param joinPoint
     */
    @Before(value = "executionService()")
    public void doServiceBefore(JoinPoint joinPoint) {
        try {
            String requestId = ThreadMdcUtil.getTraceId();
            doBeforeLog(joinPoint, requestId);
        } catch (Exception e) {
            log.error("Service方法前置通知处理异常", e);
        }
    }

    /**
     * Controller方法前置通知,调用之前调用
     *
     * @param joinPoint
     */
    @Before(value = "executionController()")
    public void doControllerBefore(JoinPoint joinPoint) {
        try {
            String requestId = ThreadMdcUtil.getTraceId();
            ServletRequestAttributes attributes = null;
            if (StringUtils.isEmpty(requestId)) {
                attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            }
            if (null != attributes) {
                HttpServletRequest request = attributes.getRequest();
                if (null != request) {
                    requestId = request.getHeader(LogConstant.LOG_TRACE_ID);
                }
            }
            doBeforeLog(joinPoint, requestId);
        } catch (Exception e) {
            log.error("Controller方法前置通知处理异常", e);
        }
    }

    /**
     * Async方法前置通知,调用之前调用
     *
     * @param joinPoint
     */
    @Before(value = "executionAsync()")
    public void doAsyncBefore(JoinPoint joinPoint) {
        try {
            String requestId = ThreadMdcUtil.getTraceId();
            doBeforeLog(joinPoint, requestId);
        } catch (Exception e) {
            log.error("Async方法前置通知处理异常", e);
        }
    }

    /**
     * 前置通知的日志处理
     *
     * @param joinPoint
     * @param logTraceId
     */
    private void doBeforeLog(JoinPoint joinPoint, String logTraceId) {
        // 设置MDC的logTraceId
        if (StringUtils.isEmpty(logTraceId)) {
            ThreadMdcUtil.setTraceId();
        } else {
            ThreadMdcUtil.setTraceId(logTraceId);
        }
        // 添加日志打印
        if (log.isDebugEnabled()) {
            log.debug("CLASS_METHOD : {}.{}()\n with argument[s] = {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    toJsonString(joinPoint.getArgs()));
        }
    }

    /**
     * 参数对象转为JSON字符串
     *
     * @param args
     * @return
     */
    private static String toJsonString(Object... args) {
        StringBuilder jsonString = new StringBuilder();
        if (null == args || args.length == 0) {
            jsonString.append("[]");
            return jsonString.toString();
        }

        jsonString.append("[");
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (i == 0) {
                jsonString.append("argument" + i + " : ");
            } else {
                jsonString.append(", argument" + i + " : ");
            }
            if (null == arg) {
                jsonString.append("null");
            } else {
                // 判断参数对象类型是否为接口或基本类型
                if (arg.getClass().isPrimitive() || arg instanceof ServletRequest
                        || arg instanceof ServletResponse) {
                    jsonString.append(arg.toString());
                } else {
                    jsonString.append(JSONObject.toJSONString(arg));
                }
            }
        }
        jsonString.append("]");
        return jsonString.toString();
    }

    /**
     * Service方法后置通知,调用之后调用
     *
     * @param joinPoint
     * @param returnValue 方法返回值
     */
    @AfterReturning(pointcut = "executionService()", returning = "returnValue")
    public void doServiceAfterReturning(JoinPoint joinPoint, Object returnValue) {
        doAfterReturning(joinPoint, returnValue);
    }

    /**
     * Controller方法后置通知,调用之后调用
     *
     * @param joinPoint
     * @param returnValue 方法返回值
     */
    @AfterReturning(pointcut = "executionController()", returning = "returnValue")
    public void doControllerAfterReturning(JoinPoint joinPoint, Object returnValue) {
        doAfterReturning(joinPoint, returnValue);
    }

    /**
     * Async方法后置通知,调用之后调用
     *
     * @param joinPoint
     * @param returnValue 方法返回值
     */
    @AfterReturning(pointcut = "executionAsync()", returning = "returnValue")
    public void doAsyncAfterReturning(JoinPoint joinPoint, Object returnValue) {
        doAfterReturning(joinPoint, returnValue);
    }

    /**
     * 方法后置通知的日志处理
     *
     * @param returnValue
     */
    private void doAfterReturning(JoinPoint joinPoint, Object returnValue) {
        try {
            // 处理完请求，返回内容
            if (null != returnValue && log.isDebugEnabled()) {
                if (returnValue instanceof String) {
                    log.debug("RESPONSE : {}", returnValue);
                } else {
                    log.debug("RESPONSE : {}", JSONObject.toJSONString(returnValue));
                }
            }
            // 清除MDC的logTraceId
            MDC.clear();
        } catch (Exception e) {
            log.error("方法后置通知处理异常", e);
        }
    }

    /**
     * Service方法的Around环绕通知,统计方法执行耗时
     *
     * @param joinPoint
     * @return
     */
    @Around("executionService()")
    public Object serviceAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return timeAround(joinPoint);
    }

    /**
     * Controller方法的Around环绕通知,统计方法执行耗时
     *
     * @param joinPoint
     * @return
     */
    @Around("executionController()")
    public Object controllerAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return timeAround(joinPoint);
    }

    /**
     * Async方法的Around环绕通知,统计方法执行耗时
     *
     * @param joinPoint
     * @return
     */
    @Around("executionAsync()")
    public Object asyncAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return timeAround(joinPoint);
    }

    /**
     * 统计方法执行耗时
     *
     * @param joinPoint
     * @return
     */
    private Object timeAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 定义返回对象、得到方法需要的参数
        // 获取开始执行的时间
        long startTime = System.currentTimeMillis();
        Object obj = joinPoint.proceed();
        // 获取执行结束的时间
        long endTime = System.currentTimeMillis();
        // 打印耗时的信息
        long diffTime = endTime - startTime;
        if (diffTime > 3000L && log.isWarnEnabled()) {
            log.warn("CLASS_METHOD : {}.{}() 处理请求共耗时: {} ms",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), diffTime);
        } else if (diffTime > 500L && log.isInfoEnabled()) {
            log.info("CLASS_METHOD : {}.{}() 处理请求共耗时: {} ms",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), diffTime);
        } else if (log.isDebugEnabled()) {
            log.debug("CLASS_METHOD : {}.{}() 处理请求共耗时: {} ms",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), diffTime);
        }
        return obj;
    }

}
