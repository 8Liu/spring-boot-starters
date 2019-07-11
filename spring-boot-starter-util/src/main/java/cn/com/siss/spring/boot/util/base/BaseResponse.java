package cn.com.siss.spring.boot.util.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @ClassName BaseResponse
 * @Description use the standard {@link /gitlab.sissyun.com.cn/frameworks/siss-common} constraint instead
 * @Author clare
 * @Date 2019/2/19 13:41
 * @Version 1.0
 */
@Deprecated
@Slf4j
@Getter
@Setter
@ToString(callSuper = true)
public class BaseResponse<T> extends Response implements Serializable {

    private T data;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (o instanceof BaseResponse) {
            BaseResponse baseResponse = (BaseResponse) o;
            if (baseResponse.getCode() .equals(this.getCode()) && baseResponse.getMessage().equals(this.getMessage()) ) return true;
            return false;
        }
        return false;
    }

}
