package cn.com.siss.spring.boot.redis.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

/**
 * 验证码工具
 *
 * @Description:
 * @Author: HJ
 * @CreateDate: 2020/4/13
 * @UpdateUser: HJ
 * @UpdateDate: 2020/4/13
 * @UpdateRemark:
 * @Version: 1.0
 */
public class RedisValidCodeUtil {

    /**
     * 校验验证码
     *
     * @param redisTemplate
     * @param cacheKey
     * @param validCode
     * @return
     */
    public static boolean checkValidCode(RedisTemplate redisTemplate,
                                  final String cacheKey,
                                  String validCode) {
        if (StringUtils.isEmpty(validCode)) {
            return false;
        }

        // 获取缓存中的验证码
        String redisCode = (String) RedisTemplateUtil.get(redisTemplate, cacheKey);
        if (StringUtils.isEmpty(redisCode)) {
            return false;
        }

        if (validCode.equals(redisCode)) {
            // 验证码验证通过, 删除验证码缓存
            RedisTemplateUtil.remove(redisTemplate, cacheKey);
            return true;
        } else {
            return false;
        }
    }
}
