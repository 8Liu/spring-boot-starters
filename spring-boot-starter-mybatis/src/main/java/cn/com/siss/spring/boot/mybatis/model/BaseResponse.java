package cn.com.siss.spring.boot.mybatis.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 接口返回基类
 *
 * @author HJ
 * @date 2019-06-21
 * @UpdateUser: HJ
 * @UpdateDate: 2019/6/22
 * @UpdateRemark:
 */
@Getter
@Setter
@ToString
public class BaseResponse implements Serializable {

    private static final long serialVersionUID = 7258031428030068691L;

    /**
     * 请求返回结果编码
     */
    protected Integer returnCode;

    /**
     * 请求返回结果消息
     */
    protected String message;

    /**
     * 操作数据信息(请求结果返回)
     */
    protected String dataInfo;

}
