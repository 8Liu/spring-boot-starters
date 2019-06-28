package cn.com.siss.spring.boot.mybatis.autoconfigration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 数据源配置
 *
 * @Description: 读取数据源配置信息
 * @Author: HJ
 * @CreateDate: 2019/6/27
 * @UpdateUser: HJ
 * @UpdateDate: 2019/6/27
 * @UpdateRemark:
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "spring")
public class DataSourceProperties {

    /**
     * 数据源基础配置
     */
    private Map<String, String> datasource;

    /**
     * 主数据源名称
     *
     * @Description: 无数据源名称则不需要配置, 如果配置了dynamicDataBase则必须配置
     * 名称必须为dynamicDataBase的数据源名的其中一个
     */
    private String mainDatabase = DataSourcePropertiesUtil.DEFAULT_SOURCE_NAME;

    /**
     * 分库数据源配置
     */
    private Map<String, Object> dynamicDataBase;
}