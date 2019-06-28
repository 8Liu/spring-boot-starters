package cn.com.siss.spring.boot.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 数据库资源访问设置注解
 *
 * @Description: 根据设定value值访问对应的数据库资源, 主要应用于简单的数据库读写分离
 * @Author: HJ
 * @CreateDate: 2019/6/28
 * @UpdateUser: HJ
 * @UpdateDate: 2019/6/28
 * @UpdateRemark:
 * @Version: 1.0
 */
@Documented
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
    // 公共组件,此处不设置默认值
    String value() default "";
}
