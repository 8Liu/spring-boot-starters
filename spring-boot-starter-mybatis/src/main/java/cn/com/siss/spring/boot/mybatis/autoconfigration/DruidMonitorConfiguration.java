package cn.com.siss.spring.boot.mybatis.autoconfigration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Druid监控配置
 *
 * @Description:
 * @Author: HJ
 * @CreateDate: 2019/6/28
 * @UpdateUser: HJ
 * @UpdateDate: 2019/6/28
 * @UpdateRemark:
 * @Version: 1.0
 */
@Configuration
@EnableConfigurationProperties(DruidMonitorProperties.class)
public class DruidMonitorConfiguration {

    @Autowired
    private DruidMonitorProperties druidMonitorProperties;

    /**
     * 注册一个StatViewServlet
     */
    @Bean
    public ServletRegistrationBean druidStatViewServlet() {
        DruidMonitorStatViewServlet statViewServlet = druidMonitorProperties.getStatViewServlet();

        //org.springframework.boot.context.embedded.ServletRegistrationBean提供类的进行注册.
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), statViewServlet.getUrlPattern());

        //添加初始化参数：initParams
        //白名单：
        servletRegistrationBean.addInitParameter("allow", statViewServlet.getAllow());

        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
        servletRegistrationBean.addInitParameter("deny", statViewServlet.getDeny());

        //登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername", statViewServlet.getLoginUsername());
        servletRegistrationBean.addInitParameter("loginPassword", statViewServlet.getLoginPassword());

        //是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", statViewServlet.getResetEnable());

        return servletRegistrationBean;
    }

    /**
     * 注册一个：filterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean druidStatFilter() {
        DruidMonitorWebStatFilter webStatFilter = druidMonitorProperties.getWebStatFilter();

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());

        //添加过滤规则.
        filterRegistrationBean.addUrlPatterns(webStatFilter.getUrlPattern());
        //添加不需要忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions", webStatFilter.getExclusions());

        return filterRegistrationBean;
    }

}
