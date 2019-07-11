package cn.com.siss.spring.boot.swagger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Swagger配置属性
 *
 * @Description: 读取Swagger配置文件值并为属性赋值
 * @Author: HJ
 * @CreateDate: 2019/6/25
 * @UpdateUser: HJ
 * @UpdateDate: 2019/6/25
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {
    /**
     * 是否启用标识
     */
    private Boolean enabled;

    /**
     * 应用服务接口文档大标题
     */
    private String title = "服务接口文档";

    /**
     * 应用API的版本号
     */
    private String version = "V1.0.0";

    /**
     * 发布API的应用服务目录
     */
    private String webBasePackage = "cn.com.siss";

    /**
     * 作者
     */
    private String author = "siss";

    /**
     * URL地址
     */
    private String url = "localhost";

    /**
     * 邮箱
     */
    private String email;

    /**
     * URL重定向标识
     */
    private Boolean redirect = false;
}
