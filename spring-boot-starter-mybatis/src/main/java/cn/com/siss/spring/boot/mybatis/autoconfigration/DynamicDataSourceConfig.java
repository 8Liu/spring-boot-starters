package cn.com.siss.spring.boot.mybatis.autoconfigration;

import cn.com.siss.spring.boot.mybatis.model.DynamicDataSource;
import cn.com.siss.spring.boot.mybatis.utils.DataSourcePropertiesUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.MapDataSourceLookup;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 数据源配置管理
 *
 * @Description:
 * @Author: HJ
 * @CreateDate: 2019/6/26
 * @UpdateUser: HJ
 * @UpdateDate: 2019/6/26
 * @UpdateRemark:
 * @Version: 1.0
 */
@Configuration
@EnableConfigurationProperties({DataSourceProperties.class, MybatisProperties.class})
public class DynamicDataSourceConfig {

    @Autowired
    private DataSourceProperties ddsProperties;
    @Autowired
    private MybatisProperties mybatisProperties;

    /**
     * spring boot 启动后将自定义创建好的数据源对象放到TargetDataSources中用于后续的切换数据源用
     * (比如：DynamicDataSourceContextHolder.setDataSourceKey("dbMall")，手动切换到dbMall数据源
     * 同时指定默认数据源连接
     *
     * @return 动态数据源对象
     */
    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() throws Exception {

        MapDataSourceLookup dataSourceLookup = new MapDataSourceLookup();

        DynamicDataSource dynamicDataSource;
        if (StringUtils.isEmpty(ddsProperties.getWebDatasourceUrl())) {
            // 数据源配置转换成动态数据源对象信息
            dynamicDataSource = DataSourcePropertiesUtil.convertDatasource(ddsProperties);
        } else {
            // 获取数据源管理系统的数据源对象信息
            dynamicDataSource = DataSourcePropertiesUtil.getWebDatasource(ddsProperties.getWebDatasourceUrl());
        }

        Map<Object, Object> dataSourceMap = new HashMap<Object, Object>();
        for (Map.Entry<String, Properties> entry : dynamicDataSource.getDynamicDatabase().entrySet()) {
            // 创建数据源对象
            DruidDataSource druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(entry.getValue());

            dataSourceLookup.addDataSource(entry.getKey(), druidDataSource);
            dataSourceMap.put(entry.getKey(), druidDataSource);
        }

        // 创建动态数据源路由器
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        // 设置数据源索引
        dataSource.setDataSourceLookup(dataSourceLookup);
        // 设定默认数据源名称
        dataSource.setDefaultTargetDataSource(dynamicDataSource.getMainDatabase());
        // 设置数据源
        dataSource.setTargetDataSources(dataSourceMap);
        return dataSource;
    }

    /**
     * 　配置mybatis的sqlSession连接动态数据源
     *
     * @param dynamicDataSource
     * @return
     * @throws Exception
     */
    @Bean
    @ConditionalOnMissingBean(SqlSessionFactory.class)
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dynamicDataSource)
            throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);

        if (!StringUtils.isEmpty(mybatisProperties.getTypeAliasesPackage())) {
            sqlSessionFactoryBean.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());
        }

        // 此处设置为了解决找不到mapper文件的问题
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources(mybatisProperties.getMapperLocations()));

        if (!StringUtils.isEmpty(mybatisProperties.getConfigLocation())) {
            sqlSessionFactoryBean.setConfigLocation(resourcePatternResolver.getResource(mybatisProperties.getConfigLocation()));
        }
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 将动态数据源添加到事务管理器中
     *
     * @param dynamicDataSource
     * @return the platform transaction manager
     */
    @Bean
    @ConditionalOnMissingBean(PlatformTransactionManager.class)
    public PlatformTransactionManager platformTransactionManager(@Qualifier("dynamicDataSource") DataSource dynamicDataSource) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }

}
