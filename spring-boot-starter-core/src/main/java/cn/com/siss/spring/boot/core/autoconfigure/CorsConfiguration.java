package cn.com.siss.spring.boot.core.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * CORS全局配置
 *
 * @author HJ
 * @version 1.0.0
 * @description Springboot使用WebMvcConfigurerAdapter的addCorsMappings的方法设置全局跨域, 可以解决大部分的跨域问题;
 * -----------> 但是加入拦截器后, 需要在拦截器中单独做跨域处理, 否则还是有跨域问题的
 * @createDate 2020/7/4
 * @updateUser HJ
 * @updateDate 2020/7/4
 * @updateRemark
 * @remark
 */
@Configuration
@EnableConfigurationProperties({CorsProperties.class})
public class CorsConfiguration extends WebMvcConfigurerAdapter {

    private static Logger logger = LoggerFactory.getLogger(CorsConfiguration.class);

    @Autowired
    private CorsProperties corsProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        String mappings = corsProperties.getMappings();
        if (null == mappings || mappings.trim() == "") {
            mappings = "/**";
        }
        logger.info("mappings is " + mappings);

        String[] allowedOrigins = corsProperties.getAllowedOrigins();
        if (null == allowedOrigins || allowedOrigins.length == 0) {
            allowedOrigins = CorsConstant.DEFAULT_ALLOWED_ORIGINS;
        }

        String[] allowedHeaders = corsProperties.getAllowedHeaders();
        if (null == allowedHeaders || allowedHeaders.length == 0) {
            allowedHeaders = CorsConstant.DEFAULT_ALLOWED_HEADERS;
        }

        String[] allowedMethods = corsProperties.getAllowedMethods();
        if (null == allowedMethods || allowedMethods.length == 0) {
            allowedMethods = CorsConstant.DEFAULT_ALLOWED_METHODS;
        }

        Boolean allowCredentials = corsProperties.getAllowCredentials();
        if (null == allowCredentials) {
            allowCredentials = CorsConstant.DEFAULT_ALLOW_CREDENTIALS;
        }

        Long maxAge = corsProperties.getMaxAge();
        if (null == maxAge || maxAge == 0) {
            maxAge = CorsConstant.DEFAULT_MAX_AGE;
        }

        registry.addMapping(mappings)
                .allowedOrigins(allowedOrigins)
                .allowedMethods(allowedMethods)
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(buildCorsInterceptor());
    }

    @Bean
    public CorsInterceptor buildCorsInterceptor() {
        CorsInterceptor interceptor = new CorsInterceptor();
        interceptor.setCorsProperties(corsProperties);
        return interceptor;
    }

}
