package cn.com.siss.spring.boot.redis.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
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
    public static <T> boolean set(RedisTemplate redisTemplate,
                                  final String key,
                                  T value) {
        BoundValueOperations<Serializable, T> operations = redisTemplate.boundValueOps(key);
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
    public static <T> boolean set(RedisTemplate redisTemplate,
                                  final String key,
                                  T value,
                                  Long expireTime) {
        BoundValueOperations<Serializable, T> operations = redisTemplate.boundValueOps(key);
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
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
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
            flag = true;
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
    public static <T> T get(RedisTemplate redisTemplate, final String key) {
        BoundValueOperations<Serializable, T> operations = redisTemplate.boundValueOps(key);
        return operations.get();
    }

    /**
     * 哈希 添加
     *
     * @param key
     * @param hashKey
     * @param value
     */
    public static <HK, HV> void hmSet(RedisTemplate redisTemplate,
                                      String key,
                                      HK hashKey,
                                      HV value) {
        BoundHashOperations<String, HK, HV> hash = redisTemplate.boundHashOps(key);
        hash.put(hashKey, value);
    }

    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    public static <HK, HV> HV hmGet(RedisTemplate redisTemplate,
                                    String key,
                                    HK hashKey) {
        BoundHashOperations<String, HK, HV> hash = redisTemplate.boundHashOps(key);
        return hash.get(hashKey);
    }

    /**
     * 列表添加
     *
     * @param key
     * @param dataValue
     */
    public static <T> void addListData(RedisTemplate redisTemplate,
                                       String key,
                                       T dataValue) {
        addListData(redisTemplate, key, dataValue, null);
    }

    /**
     * 列表添加
     *
     * @param key
     * @param dataValues
     */
    public static <T> void addListData(RedisTemplate redisTemplate,
                                       String key,
                                       List<T> dataValues) {
        addListData(redisTemplate, key, dataValues, null);
    }

    /**
     * 列表添加
     *
     * @param key
     * @param dataValue
     * @param expireTime 有效时间(秒)
     */
    public static <T> void addListData(RedisTemplate redisTemplate,
                                       String key,
                                       T dataValue,
                                       Long expireTime) {
        BoundListOperations<String, T> list = redisTemplate.boundListOps(key);
        list.rightPush(dataValue);
        if (null != expireTime) {
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        }
    }

    /**
     * 列表添加
     *
     * @param key
     * @param dataValues
     * @param expireTime 有效时间(秒)
     */
    public static <T> void addListData(RedisTemplate redisTemplate,
                                       String key,
                                       List<T> dataValues,
                                       Long expireTime) {
        BoundListOperations<String, T> list = redisTemplate.boundListOps(key);
        for (T dataValue : dataValues) {
            list.rightPush(dataValue);
        }
        if (null != expireTime) {
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        }
    }

    /**
     * 列表获取
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static <T> List<T> getListData(RedisTemplate redisTemplate,
                                          String key,
                                          long start,
                                          long end) {
        BoundListOperations<String, T> list = redisTemplate.boundListOps(key);
        return list.range(start, end);
    }

    /**
     * 集合添加
     *
     * @param key
     * @param dataValue
     */
    public static <T> void addSetData(RedisTemplate redisTemplate,
                                      String key,
                                      T dataValue) {
        addSetData(redisTemplate, key, dataValue, null);
    }

    /**
     * 集合添加
     *
     * @param key
     * @param dataValues
     */
    public static <T> void addSetData(RedisTemplate redisTemplate,
                                      String key,
                                      Collection<T> dataValues) {
        addSetData(redisTemplate, key, dataValues, null);
    }

    /**
     * 集合添加
     *
     * @param key
     * @param dataValue
     * @param expireTime 有效时间(秒)
     */
    public static <T> void addSetData(RedisTemplate redisTemplate,
                                      String key,
                                      T dataValue,
                                      Long expireTime) {
        BoundSetOperations<String, T> set = redisTemplate.boundSetOps(key);
        set.add(dataValue);
        if (null != expireTime) {
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        }
    }

    /**
     * 集合添加
     *
     * @param key
     * @param dataValues
     * @param expireTime 有效时间(秒)
     */
    public static <T> void addSetData(RedisTemplate redisTemplate,
                                      String key,
                                      Collection<T> dataValues,
                                      Long expireTime) {
        BoundSetOperations<String, T> set = redisTemplate.boundSetOps(key);
        for (T dataValue : dataValues) {
            set.add(dataValue);
        }
        if (null != expireTime) {
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        }
    }

    /**
     * 集合获取
     *
     * @param key
     * @return
     */
    public static <T> Set<T> getSetData(RedisTemplate redisTemplate, String key) {
        BoundSetOperations<String, T> set = redisTemplate.boundSetOps(key);
        return set.members();
    }

    /**
     * 删除集合数据
     *
     * @param key
     * @param dataValue
     * @param <T>
     * @return
     */
    public static <T> Long removeSetData(RedisTemplate redisTemplate, String key, T dataValue) {
        BoundSetOperations<String, T> set = redisTemplate.boundSetOps(key);
        return set.remove(dataValue);
    }

    /**
     * 删除集合数据
     *
     * @param key
     * @param dataValue
     * @param <T>
     * @return
     */
    public static <T> Long removeSetData(RedisTemplate redisTemplate, String key, Collection<T> dataValue) {
        BoundSetOperations<String, T> set = redisTemplate.boundSetOps(key);
        return set.remove(dataValue);
    }

    /**
     * 校验集合数据
     *
     * @param key
     * @param dataValue
     * @param <T>
     * @return
     */
    public static <T> Boolean checkSetData(RedisTemplate redisTemplate,
                                           String key,
                                           T dataValue) {
        BoundSetOperations<String, T> set = redisTemplate.boundSetOps(key);
        return set.isMember(dataValue);
    }

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param score
     */
    public static <T> Boolean addZSetData(RedisTemplate redisTemplate, String key, T value, double score) {
        return addZSetData(redisTemplate, key, value, score, null);
    }

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param score
     * @param expireTime 有效时间(秒)
     */
    public static <T> Boolean addZSetData(RedisTemplate redisTemplate,
                                          String key,
                                          T value,
                                          double score,
                                          Long expireTime) {
        BoundZSetOperations<String, T> zSet = redisTemplate.boundZSetOps(key);
        Boolean flag = zSet.add(value, score);
        if (null != expireTime) {
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        }
        return flag;
    }

    /**
     * 有序集合获取
     *
     * @param key
     * @param minScore
     * @param maxScore
     * @return
     */
    public static <T> Set<T> getZSetDataByScore(RedisTemplate redisTemplate, String key, double minScore, double maxScore) {
        BoundZSetOperations<String, T> zSet = redisTemplate.boundZSetOps(key);
        Set<T> dataSet = zSet.rangeByScore(minScore, maxScore);
        if (!CollectionUtils.isEmpty(dataSet)) {
            zSet.removeRangeByScore(minScore, maxScore);
        }
        return dataSet;
    }

    /**
     * 删除集合数据
     *
     * @param key
     * @param dataValue
     * @param <T>
     * @return
     */
    public static <T> Long removeZSetData(RedisTemplate redisTemplate, String key, T dataValue) {
        BoundZSetOperations<String, T> zSet = redisTemplate.boundZSetOps(key);
        return zSet.remove(dataValue);
    }

    /**
     * 删除集合数据
     *
     * @param key
     * @param setData
     * @param <T>
     * @return
     */
    public static <T> Long removeZSetData(RedisTemplate redisTemplate, String key, Collection<T> setData) {
        BoundZSetOperations<String, T> zSet = redisTemplate.boundZSetOps(key);
        return zSet.remove(setData);
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