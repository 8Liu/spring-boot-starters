package cn.com.siss.spring.boot.mybatis.utils;

import cn.com.siss.spring.boot.mybatis.autoconfigration.DataSourceProperties;
import cn.com.siss.spring.boot.mybatis.model.DynamicDataSource;
import cn.com.siss.spring.boot.mybatis.model.BaseResponse;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

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
@Slf4j
public class DataSourcePropertiesUtil {

    /**
     * 默认数据源名称
     */
    public static final String DEFAULT_SOURCE_NAME = "single";

    /**
     * 接口返回值: 数据处理成功[1000]
     */
    public static final Integer CODE_1000 = 1000;

    /**
     * 数据源配置转换成动态数据源对象信息
     *
     * @param ddsProperties 数据源配置
     * @return
     */
    public static final DynamicDataSource convertDatasource(DataSourceProperties ddsProperties) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();

        Map<String, Properties> allMap = new HashMap<String, Properties>();
        if (null == ddsProperties.getDynamicDataBase() || ddsProperties.getDynamicDataBase().isEmpty()) {
            // 单数据源
            Properties entryProperties = new Properties();
            entryProperties.putAll(ddsProperties.getDatasource());
            allMap.put(DEFAULT_SOURCE_NAME, entryProperties);
            // 设置主数据源名称
            dynamicDataSource.setMainDatabase(DEFAULT_SOURCE_NAME);
        } else {
            // 设置主数据源名称
            dynamicDataSource.setMainDatabase(ddsProperties.getMainDatabase());
            // 多数据源
            for (Entry<String, Object> entry : ddsProperties.getDynamicDataBase().entrySet()) {
                // 复制通用的数据源配置
                Properties entryProperties = new Properties();
                entryProperties.putAll(ddsProperties.getDatasource());

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
        }
        dynamicDataSource.setDynamicDatabase(allMap);
        return dynamicDataSource;
    }

    /**
     * 获取数据源管理系统的数据源对象信息
     *
     * @param webDatasourceUrl
     * @return
     * @throws Exception
     */
    public static DynamicDataSource getWebDatasource(String webDatasourceUrl) throws Exception {
        DynamicDataSource dynamicDataSource = null;
        // 请求网络通讯
        log.info("webDatasourceUrl: " + webDatasourceUrl);
        String responseStr = HttpRequestUtil.sendPost(webDatasourceUrl, null);
        log.info("responseStr: " + responseStr);
        if (!StringUtils.isEmpty(responseStr)) {
            BaseResponse baseResponse = JSONObject.parseObject(responseStr, BaseResponse.class);
            if (null != baseResponse && CODE_1000.equals(baseResponse.getReturnCode())
                    && !StringUtils.isEmpty(baseResponse.getDataInfo())) {
                dynamicDataSource = JSONObject.parseObject(baseResponse.getDataInfo(), DynamicDataSource.class);
            }
        }
        if (null == dynamicDataSource) {
            Map<String, Properties> allMap = new HashMap<String, Properties>();
            dynamicDataSource = new DynamicDataSource();
            dynamicDataSource.setDynamicDatabase(allMap);
        }
        return dynamicDataSource;
    }
}
