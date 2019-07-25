package cn.com.siss.spring.boot.mybatis.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

/**
 * 动态数据源对象信息
 *
 * @Description:
 * @Author: HJ
 * @CreateDate: 2019/7/23
 * @UpdateUser: HJ
 * @UpdateDate: 2019/7/23
 * @UpdateRemark:
 * @Version: 1.0
 */
@Getter
@Setter
@ToString
public class DynamicDataSource implements Serializable {
    /**
     * 主数据源名称
     */
    private String mainDatabase;

    /**
     * 分库数据源配置
     */
    private Map<String, Properties> dynamicDatabase;
}
