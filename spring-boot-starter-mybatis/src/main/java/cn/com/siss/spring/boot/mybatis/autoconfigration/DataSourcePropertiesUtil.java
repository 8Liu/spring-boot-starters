package cn.com.siss.spring.boot.mybatis.autoconfigration;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * 数据源配置转化处理工具类
 *
 * @Description:
 * @Author: HJ
 * @CreateDate: 2019/6/27
 * @UpdateUser: HJ
 * @UpdateDate: 2019/6/27
 * @UpdateRemark:
 * @Version: 1.0
 */
public class DataSourcePropertiesUtil {

    /**
     * 默认数据源名称
     */
    public static final String DEFAULT_SOURCE_NAME = "single";

    public static final Map<String, Properties> prop2DBMap(Map<String, String> properties,
                                                           Map<String, Object> dynamicDataBase) {
        Map<String, Properties> allMap = new HashMap<String, Properties>();

        if (null == dynamicDataBase || dynamicDataBase.isEmpty()) {
            // 单数据源
            Properties entryProperties = new Properties();
            entryProperties.putAll(properties);
            allMap.put(DEFAULT_SOURCE_NAME, entryProperties);
            return allMap;
        }

        // 多数据源
        for (Entry<String, Object> entry : dynamicDataBase.entrySet()) {
            // 复制通用的数据源配置
            Properties entryProperties = new Properties();
            entryProperties.putAll(properties);

            // 获取数据源独立配置信息
            if (null != entry.getValue() && entry.getValue() instanceof Map) {
                Map<String, String> dataConfigMap = (Map<String, String>) entry.getValue();
                // 设置数据源的独立配置项
                for (Entry<String, String> dataEntry : dataConfigMap.entrySet()) {
                    entryProperties.put(dataEntry.getKey(), dataEntry.getValue());
                }
            }
            allMap.put(entry.getKey(), entryProperties);
        }
        return allMap;
    }
}
