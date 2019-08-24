package cn.com.siss.spring.boot.logging;

import cn.com.siss.framework.common.constant.LogConstant;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class ThreadMdcUtil {

    /**
     * 创建LOG_TRACE_ID
     *
     * @return
     */
    public static String createTraceId() {
        return UUID.randomUUID().toString();
    }

    /**
     * 设置LOG_TRACE_ID
     */
    public static void setTraceId() {
        setTraceId(createTraceId());
    }

    /**
     * 设置LOG_TRACE_ID
     *
     * @param traceId
     */
    public static void setTraceId(String traceId) {
        MDC.put(LogConstant.LOG_TRACE_ID, traceId);
    }

    /**
     * 设置LOG_TRACE_ID
     */
    public static void setTraceIdIfAbsent() {
        String traceId = MDC.get(LogConstant.LOG_TRACE_ID);
        if (StringUtils.isEmpty(traceId)) {
            setTraceId();
        }
    }

    /**
     * 获取LOG_TRACE_ID值
     *
     * @return
     */
    public static String getTraceId() {
        return MDC.get(LogConstant.LOG_TRACE_ID);
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
