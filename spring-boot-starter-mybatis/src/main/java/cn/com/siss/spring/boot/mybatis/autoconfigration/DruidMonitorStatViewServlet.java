package cn.com.siss.spring.boot.mybatis.autoconfigration;

import lombok.Data;

/**
 * Druid监控的StatViewServlet配置属性
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
public class DruidMonitorStatViewServlet {
    /**
     * 是否启用StatViewServlet默认值true
     */
    private String enabled = "true";

    /**
     * URL Mappings映射
     */
    private String urlPattern = "/druid/*";

    /**
     * 白名单
     */
    private String allow = "";
    /**
     * IP黑名单(存在共同时，deny优先于allow)
     */
    private String deny = "";
    /**
     * 登录查看信息的账号
     */
    private String loginUsername = "siss";

    /**
     * 登录查看信息的密码
     */
    private String loginPassword = "admin";

    /**
     * 是否能够重置数据
     */
    private String resetEnable = "false";
}
