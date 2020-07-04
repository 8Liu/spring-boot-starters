package cn.com.siss.spring.boot.core.autoconfigure;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求拦截器, 处理跨域问题
 *
 * @author HJ
 * @version 1.0.0
 * @description
 * @createDate 2020/7/4
 * @updateUser HJ
 * @updateDate 2020/7/4
 * @updateRemark
 * @remark
 */
public class CorsInterceptor implements HandlerInterceptor {

    private CorsProperties corsProperties;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler)
            throws Exception {
        String allowedHeaders;
        String[] allowedHeaderArr = corsProperties.getAllowedHeaders();
        if (null != allowedHeaderArr && allowedHeaderArr.length > 0) {
            allowedHeaders = CorsConstant.SIMPLE_ALLOWED_HEADERS.concat(",")
                    .concat(String.join(",", allowedHeaderArr));
        } else {
            allowedHeaders = CorsConstant.SIMPLE_ALLOWED_HEADERS;
        }
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", allowedHeaders);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "86400");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView)
            throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {

    }

    public void setCorsProperties(CorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }
}
