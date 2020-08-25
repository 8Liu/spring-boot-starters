package cn.com.siss.spring.boot.redis.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisTemplateUtil {
    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean set(RedisTemplate redisTemplate,
                              final String key,
                              Object value) {
        BoundValueOperations<Serializable, Object> operations = redisTemplate.boundValueOps(key);
        operations.set(value);
        return true;
    }

    /**
     * 写入缓存设置时效时间
     *
     * @param key
     * @param value
     * @param expireTime 有效时间(秒)
     * @return
     */
    public static boolean set(RedisTemplate redisTemplate,
                              final String key,
                              Object value,
                              Long expireTime) {
        BoundValueOperations<Serializable, Object> operations = redisTemplate.boundValueOps(key);
        operations.set(value, expireTime, TimeUnit.SECONDS);
        return true;
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public static void remove(RedisTemplate redisTemplate, final String... keys) {
        for (String key : keys) {
            remove(redisTemplate, key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public static void removePattern(RedisTemplate redisTemplate, final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public static Boolean remove(RedisTemplate redisTemplate, final String key) {
        Boolean flag = false;
        if (exists(redisTemplate, key)) {
            redisTemplate.delete(key);
        }
        return flag;
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public static boolean exists(RedisTemplate redisTemplate, final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public static Object get(RedisTemplate redisTemplate, final String key) {
        BoundValueOperations<Serializable, Object> operations = redisTemplate.boundValueOps(key);
        return operations.get();
    }

    /**
     * 哈希 添加
     *
     * @param key
     * @param hashKey
     * @param value
     */
    public static void hmSet(RedisTemplate redisTemplate, String key, Object hashKey, Object value) {
        BoundHashOperations<String, Object, Object> hash = redisTemplate.boundHashOps(key);
        hash.put(hashKey, value);
    }

    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    public static Object hmGet(RedisTemplate redisTemplate, String key, Object hashKey) {
        BoundHashOperations<String, Object, Object> hash = redisTemplate.boundHashOps(key);
        return hash.get(hashKey);
    }

    /**
     * 列表添加
     *
     * @param key
     * @param value
     */
    public static void lPush(RedisTemplate redisTemplate, String key, Object value) {
        BoundListOperations<String, Object> list = redisTemplate.boundListOps(key);
        list.rightPush(value);
    }

    /**
     * 列表获取
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static List<Object> lRange(RedisTemplate redisTemplate, String key, long start, long end) {
        BoundListOperations<String, Object> list = redisTemplate.boundListOps(key);
        return list.range(start, end);
    }

    /**
     * 集合添加
     *
     * @param key
     * @param value
     */
    public static Long add(RedisTemplate redisTemplate, String key, Object value) {
        BoundSetOperations<String, Object> set = redisTemplate.boundSetOps(key);
        Long flag = set.add(value);
        return flag;
    }

    /**
     * 集合获取
     *
     * @param key
     * @return
     */
    public static Set<Object> setMembers(RedisTemplate redisTemplate, String key) {
        BoundSetOperations<String, Object> set = redisTemplate.boundSetOps(key);
        return set.members();
    }

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    public static Boolean zAdd(RedisTemplate redisTemplate, String key, Object value, double scoure) {
        BoundZSetOperations<String, Object> zset = redisTemplate.boundZSetOps(key);
        Boolean flag = zset.add(value, scoure);
        return flag;
    }

    /**
     * 有序集合获取
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public static Set<Object> rangeByScore(RedisTemplate redisTemplate, String key, double scoure, double scoure1) {
        BoundZSetOperations<String, Object> zset = redisTemplate.boundZSetOps(key);
        return zset.rangeByScore(scoure, scoure1);
    }

    /**
     * @param key
     * @param liveTime
     * @return
     */
    public Long incr(RedisTemplate redisTemplate, String key, long liveTime) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();
        entityIdCounter.getAndDecrement();
        if ((null == increment || increment.longValue() == 0) && liveTime > 0) {//初始设置过期时间
            entityIdCounter.expire(liveTime, TimeUnit.SECONDS);
        }
        log.info("自增加一:{}", increment);
        return increment;
    }

    /**
     * @param key
     * @param liveTime
     * @return
     */
    public Long decr(RedisTemplate redisTemplate, String key, long liveTime) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndDecrement();
        if ((null == increment || increment.longValue() == 0) && liveTime > 0) {//初始设置过期时间
            entityIdCounter.expire(liveTime, TimeUnit.SECONDS);
        }
        log.info("减法:{}", increment);
        return increment;
    }


}