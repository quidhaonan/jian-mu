package com.lmyxlf.jian_mu.global.util;

import cn.hutool.core.collection.CollUtil;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/29 20:45
 * @description redis 工具类
 * @since 17
 */
@Component
public class RedisUtil {

    private static RedissonClient redissonClient;

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {

        RedisUtil.redissonClient = redissonClient;
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public static <T> void set(final String key, final T value) {

        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public static <T> void set(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {

        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.set(value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key     redis 键
     * @param timeout 超时时间
     * @return true：设置成功，false：设置失败
     */
    public static boolean expire(final String key, final long timeout) {

        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true：设置成功，false：设置失败
     */
    public static boolean expire(final String key, final long timeout, final TimeUnit unit) {

        RBucket<Object> bucket = redissonClient.getBucket(key);
        return bucket.expire(timeout, unit);
    }

    /**
     * 获取有效时间
     *
     * @param key redis 键
     * @return 有效时间
     */
    public static long getExpire(final String key) {

        return redissonClient.getBucket(key).remainTimeToLive();
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true：存在，false：不存在
     */
    public static Boolean hasKey(String key) {

        return redissonClient.getBucket(key).isExists();
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public static <T> T get(final String key) {

        RBucket<T> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public static boolean delete(final String key) {

        return redissonClient.getBucket(key).delete();
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public static boolean delete(final Collection<String> collection) {

        return redissonClient.getKeys().delete(
                collection.toArray(String[]::new)) > 0;
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的 list 数据
     * @return 缓存的对象
     */
    public static <T> long setCacheList(final String key, final List<T> dataList) {

        RList<T> rList = redissonClient.getList(key);
        rList.addAll(dataList);
        return rList.size();
    }

    /**
     * 获得缓存的 list 对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public static <T> List<T> getCacheList(final String key) {

        RList<T> rList = redissonClient.getList(key);
        return rList.readAll();
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public static <T> RSet<T> setCacheSet(final String key, final Set<T> dataSet) {

        RSet<T> rSet = redissonClient.getSet(key);
        rSet.addAll(dataSet);
        return rSet;
    }

    /**
     * 获得缓存的 set
     *
     * @param key
     * @return
     */
    public static <T> Set<T> getCacheSet(final String key) {

        RSet<T> rSet = redissonClient.getSet(key);
        return rSet.readAll();
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public static <T> void setCacheMap(final String key, final Map<String, T> dataMap) {

        if (CollUtil.isNotEmpty(dataMap)) {

            RMap<String, T> rMap = redissonClient.getMap(key);
            rMap.putAll(dataMap);
        }
    }

    /**
     * 获得缓存的 map
     *
     * @param key
     * @return
     */
    public static <T> Map<String, T> getCacheMap(final String key) {

        RMap<String, T> rMap = redissonClient.getMap(key);
        return rMap.readAllMap();
    }

    /**
     * 往 hash 中存入数据
     *
     * @param key   redis 键
     * @param hKey  hash 键
     * @param value 值
     */
    public static <T> void setCacheMapValue(final String key, final String hKey, final T value) {

        RMap<String, T> rMap = redissonClient.getMap(key);
        rMap.put(hKey, value);
    }

    /**
     * 获取 hash 中的数据
     *
     * @param key  redis 键
     * @param hKey hash 键
     * @return hash 中的对象
     */
    public static <T> T getCacheMapValue(final String key, final String hKey) {

        RMap<String, T> rMap = redissonClient.getMap(key);
        return rMap.get(hKey);
    }

    /**
     * 获取多个 hash 中的数据
     *
     * @param key   redis键
     * @param hKeys hash 键集合
     * @return hash 对象集合
     */
    public static <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys) {

        RMap<Object, T> rMap = redissonClient.getMap(key);
        return hKeys.stream()
                .map(rMap::get)
                .collect(Collectors.toList());
    }

    /**
     * 删除 hash 中的某条数据
     *
     * @param key  redis 键
     * @param hKey hash 键
     * @return 是否成功
     */
    public static boolean deleteCacheMapValue(final String key, final String hKey) {

        RMap<Object, Object> rMap = redissonClient.getMap(key);
        return rMap.fastRemove(hKey) > 0;
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public static Collection<String> keys(final String pattern) {

        RKeys rKeys = redissonClient.getKeys();
        Iterable<String> iterableKeys = rKeys.getKeysByPattern(pattern);
        return StreamSupport.stream(iterableKeys.spliterator(), false)
                .collect(Collectors.toList());
    }
}