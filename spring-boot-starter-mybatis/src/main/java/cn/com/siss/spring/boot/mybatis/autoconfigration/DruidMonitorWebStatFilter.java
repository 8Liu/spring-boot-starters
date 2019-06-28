package cn.com.siss.spring.boot.mybatis.autoconfigration;

import lombok.Data;

/**
 * Druid监控的WebStatFilter配置属性
 *
 * @Description:
 * @Author: HJ
 * @CreateDate: 2019/6/28
 * @UpdateUser: HJ
 * @UpdateDate: 2019/6/28
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class DruidMonitorWebStatFilter {
    /**
     * 是否启用StatFilter默认值true
     */
    private String enabled = "true";

    /**
     * URL过滤规则
     */
    private String urlPattern = "/*";

    /**
     * 不需要忽略的格式信息
     */
    private String exclusions = "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid2/*";

    private String sessionStatEnable = "";
    private String sessionStatMaxCount = "";
    private String principalSessionName = "";
    private String principalCookieName = "";
    private String profileEnable = "";
}
