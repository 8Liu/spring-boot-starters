package cn.com.siss.spring.boot.util.base.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Description use the standard {@link /gitlab.sissyun.com.cn/frameworks/siss-common} constraint instead
 */
@Deprecated
@Setter
@Getter
public class BasePageRequestParam implements Serializable {

    private String shopId;

    private Integer pageSize;

    private Integer pageNum;




}
