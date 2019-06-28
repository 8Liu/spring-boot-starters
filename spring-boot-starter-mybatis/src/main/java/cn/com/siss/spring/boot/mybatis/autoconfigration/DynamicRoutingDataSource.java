package cn.com.siss.spring.boot.mybatis.autoconfigration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;

/**
 * 动态数据源路由器
 *
 * @Description: 分配数据源的路由, 继承AbstractRoutingDataSource
 * @Author: HJ
 * @CreateDate: 2019/6/26
 * @UpdateUser: HJ
 * @UpdateDate: 2019/6/26
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    /**
     * 获取[确认]当前数据源的Key
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        // 从ThreadLocal中获取当前数据源的Key
        Object dataSourceKey = DynamicDataSourceHolder.getDataSourceKey();
        log.debug("current DataSourceKey : [{}]", dataSourceKey);
        return dataSourceKey;
    }

    /**
     * 设置数据源
     *
     * @param targetDataSources
     */
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        DynamicDataSourceHolder.getDataSourceMap().putAll(targetDataSources);
        // super.afterPropertiesSet();// 必须添加该句，否则服务启动后新添加数据源无法识别到
    }
}
