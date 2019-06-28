package cn.com.siss.spring.boot.mybatis.autoconfigration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Set;

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
@EnableConfigurationProperties({DataSourceProperties.class})
public class DynamicDataSourceConfig {

    @Autowired
    private DataSourceProperties ddsProperties;

    @Value("${mybatis.typeAliasesPackage:}")
    private String typeAliasesPackage;

    @Value("${mybatis.mapperLocations}")
    private String mapperLocations;

    @Value("${mybatis.configLocation:}")
    private String configLocation;

    /**
     * 初始化数据源对象
     *
     * @return
     * @throws Exception
     */
    private Map<Object, Object> initDataSource() throws Exception {
        Map<Object, Object> dbMap = new HashMap<Object, Object>();
        Map<String, Properties> prop2dbMap = DataSourcePropertiesUtil.prop2DBMap(ddsProperties.getDatasource(),
                ddsProperties.getDynamicDataBase());
        Set<Map.Entry<String, Properties>> entrySet = prop2dbMap.entrySet();
        for (Map.Entry<String, Properties> entry : entrySet) {
            // 创建数据源对象
            DruidDataSource db = (DruidDataSource) DruidDataSourceFactory.createDataSource(entry.getValue());
            dbMap.put(entry.getKey(), db);
        }
        return dbMap;
    }

    /**
     * spring boot 启动后将自定义创建好的数据源对象放到TargetDataSources中用于后续的切换数据源用
     * (比如：DynamicDataSourceContextHolder.setDataSourceKey("dbMall")，手动切换到dbMall数据源
     * 同时指定默认数据源连接
     *
     * @return 动态数据源对象
     */
    @Bean
    public DataSource dynamicDataSource() throws Exception {

        MapDataSourceLookup dataSourceLookup = new MapDataSourceLookup();
        Map<Object, Object> initDataSourceMap = initDataSource();
        for (Map.Entry<Object, Object> entry : initDataSourceMap.entrySet()) {
            dataSourceLookup.addDataSource((String) entry.getKey(), (DataSource) entry.getValue());
        }

        // 创建动态数据源路由器
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setDataSourceLookup(dataSourceLookup);
        // 设定默认数据源
        dataSource.setDefaultTargetDataSource(ddsProperties.getMainDatabase());
        dataSource.setTargetDataSources(initDataSourceMap);
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

        if (!StringUtils.isEmpty(typeAliasesPackage)) {
            sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
        }

        // 此处设置为了解决找不到mapper文件的问题
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources(mapperLocations));

        if (!StringUtils.isEmpty(configLocation)) {
            sqlSessionFactoryBean.setConfigLocation(resourcePatternResolver.getResource(configLocation));
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
