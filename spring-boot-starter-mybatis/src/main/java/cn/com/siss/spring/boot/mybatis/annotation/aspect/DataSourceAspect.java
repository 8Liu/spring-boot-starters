package cn.com.siss.spring.boot.mybatis.annotation.aspect;

import cn.com.siss.spring.boot.mybatis.annotation.DataSource;
import cn.com.siss.spring.boot.mybatis.autoconfigration.DataSourceProperties;
import cn.com.siss.spring.boot.mybatis.autoconfigration.DynamicDataSourceHolder;
import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 数据库资源访问设置DataSource注解的Aspect类
 *
 * @Description: 根据DataSource注解设定value值访问对应的数据库资源, 主要应用于简单的数据库读写分离
 * @Author: HJ
 * @CreateDate: 2019/6/28
 * @UpdateUser: HJ
 * @UpdateDate: 2019/6/28
 * @UpdateRemark:
 * @Version: 1.0
 */
@Slf4j
@Aspect
@Order(-1)
@Component
@Configuration
public class DataSourceAspect {

    @Autowired
    private DataSourceProperties dataSourceMap;


    @Before(value = "@annotation(dataSource)")
    public void before(JoinPoint joinPoint, DataSource dataSource) throws Throwable {

        String dataSourceKey = dataSource.value();
        if (StringUtils.isEmpty(dataSourceKey) || !DynamicDataSourceHolder.isExistDataSource(dataSourceKey)) {
            log.debug("数据源[{}]不存在，使用默认数据源", dataSource.value());
            DynamicDataSourceHolder.setDataSourceKey(dataSourceMap.getMainDatabase());
        } else {
            DynamicDataSourceHolder.setDataSourceKey(dataSourceKey);
        }
    }

    @After("@annotation(cn.com.siss.spring.boot.mybatis.annotation.DataSource)")
    public void doAfter(JoinPoint joinPoint) {
        log.debug("====>缓存清理");
        DynamicDataSourceHolder.clearDataSourceKey();
    }

}
