package cn.com.siss.spring.boot.mybatis.autoconfigration;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源上下文路由管理器
 *
 * @Description: 动态数据源的切换主要是通过调用这个类的方法来完成的。
 * 在任何想要进行切换数据源的时候都可以通过调用这个类的方法实现切换。
 * 比如系统登录时, 根据用户信息调用这个类的数据源切换方法切换到用户对应的数据库。
 * @Author: HJ
 * @CreateDate: 2019/6/26
 * @UpdateUser: HJ
 * @UpdateDate: 2019/6/26
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class DynamicDataSourceHolder {

    /**
     * 数据源线程
     *
     * @remark 当使用ThreadLocal维护变量时, ThreadLocal为每个使用该变量的线程提供独立的变量副本,
     * 所以每一个线程都可以独立地改变自己的副本, 而不会影响其它线程所对应的副本.
     */
    private static final ThreadLocal<String> currentDataSource = new ThreadLocal<String>();

    /**
     * 用于存储已实例的数据源
     */
    private static Map<Object, Object> dataSourceMap = new HashMap<Object, Object>();

    /**
     * 切换数据源名
     *
     * @param dataSourceKey
     */
    public static void setDataSourceKey(String dataSourceKey) {
        currentDataSource.set(dataSourceKey);
    }

    /**
     * 获取当前数据源名
     *
     * @return
     */
    public static String getDataSourceKey() {
        return currentDataSource.get();
    }

    /**
     * 清除当前数据源名
     */
    public static void clearDataSourceKey() {
        currentDataSource.remove();
    }

    /**
     * 获取存储已实例的数据源map
     *
     * @return
     */
    public static Map<Object, Object> getDataSourceMap() {
        return dataSourceMap;
    }

    /**
     * 是否存在当前key的 DataSource
     *
     * @param key
     * @return 存在返回 true, 不存在返回 false
     */
    public static boolean isExistDataSource(String key) {
        return dataSourceMap.containsKey(key);
    }

}
