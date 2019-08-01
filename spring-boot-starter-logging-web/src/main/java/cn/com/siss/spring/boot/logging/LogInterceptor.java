package cn.com.siss.spring.boot.logging;

import cn.com.siss.framework.common.constant.LogConstant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志拦截器
 *
 * @Description:
 * @Author: HJ
 * @CreateDate: 2019/7/29
 * @UpdateUser: HJ
 * @UpdateDate: 2019/7/29
 * @UpdateRemark:
 * @Version: 1.0
 */
@Slf4j
public class LogInterceptor extends HandlerInterceptorAdapter {

    /**
     * 请求处理完毕回调方法
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex)
            throws Exception {
        try {
            // 删除日志跟踪ID
            MDC.remove(LogConstant.LOG_TRACE_ID);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    /**
     * 预处理回调方法
     *
     * @param request
     * @param response
     * @param handler  响应的处理器, 自定义Controller
     * @return true: 表示继续流程(如调用下一个拦截器或处理器); false: 表示流程中断(如登录检查失败),不会继续调用其他的拦截器或处理器,此时我们需要通过response来产生响应;
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler)
            throws Exception {
        String traceId = request.getHeader(LogConstant.LOG_TRACE_ID);
        if (StringUtils.isEmpty(traceId)) {
            traceId = ThreadMdcUtil.createTraceId();
        }
        MDC.put(LogConstant.LOG_TRACE_ID, traceId);
        return true;
    }
}
