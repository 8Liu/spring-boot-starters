package cn.com.siss.spring.boot.core.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by  siss on 16-10-9.
 * PackageName
 * ModifyDate  16-10-9
 * Description (cors prop 配置类)
 * ProjectName
 */
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {

    private boolean enabled;

    private String mappings;

    /**
     * 支持的域集合, 例如"http://domain1.com"注意域名末尾不需要加斜杠
     * <p>这些值都显示在请求头中的Access-Control-Allow-Origin
     * "*"代表所有域的请求都支持
     * <p>如果没有定义, 所有请求的域都支持
     */
    private String[] allowedOrigins;

    /**
     * 支持的请求方法，例如"{RequestMethod.GET, RequestMethod.POST}"}。
     * 默认支持RequestMapping中设置的[GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE]方法
     */
    private String[] allowedMethods;

    /**
     * 允许的请求头header, 非简单请求(not-so-simple request)的消息头;
     * 组件已增加了简单请求(simple request)消息头"Accept,Accept-Language,Content-Language,Last-Event-ID,Content-Type,logTraceId"
     */
    private String[] allowedHeaders;

    /**
     * 响应头中允许访问的header, 默认为空
     */
    private String[] exposedHeaders;

    /**
     * 是否允许cookie随请求发送，使用时必须指定具体的域
     */
    private Boolean allowCredentials;

    /**
     * 预请求的结果的有效期, 默认30分钟
     */
    private Long maxAge;

    public String[] getAllowedOrigins() {

        return allowedOrigins;
    }

    public void setAllowedOrigins(String[] allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public String[] getAllowedMethods() {
        return allowedMethods;
    }

    public void setAllowedMethods(String[] allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public String[] getAllowedHeaders() {
        return allowedHeaders;
    }

    public void setAllowedHeaders(String[] allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public String[] getExposedHeaders() {
        return exposedHeaders;
    }

    public void setExposedHeaders(String[] exposedHeaders) {
        this.exposedHeaders = exposedHeaders;
    }

    public Boolean getAllowCredentials() {
        return allowCredentials;
    }

    public void setAllowCredentials(Boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }

    public Long getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Long maxAge) {
        this.maxAge = maxAge;
    }

    public String getMappings() {
        return mappings;
    }

    public void setMappings(String mappings) {
        this.mappings = mappings;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

