package com.lmyxlf.jian_mu.admin.service.impl;

import com.lmyxlf.jian_mu.admin.constant.CacheConstant;
import com.lmyxlf.jian_mu.admin.model.resp.RespMonitorCache;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysCache;
import com.lmyxlf.jian_mu.admin.service.CacheService;
import com.lmyxlf.jian_mu.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/4 22:56
 * @description
 * @since 17
 */
@Slf4j
@Service("cacheService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CacheServiceImpl implements CacheService {

    private final RedisTemplate<String, String> redisTemplate;

    private final static List<RespSysCache> CACHES = new ArrayList<>();

    {
        CACHES.add(new RespSysCache(CacheConstant.LOGIN_TOKEN_KEY, "用户信息"));
        CACHES.add(new RespSysCache(CacheConstant.SYS_CONFIG_KEY, "配置信息"));
        CACHES.add(new RespSysCache(CacheConstant.SYS_DICT_KEY, "数据字典"));
        CACHES.add(new RespSysCache(CacheConstant.CAPTCHA_CODE_KEY, "验证码"));
        CACHES.add(new RespSysCache(CacheConstant.REPEAT_SUBMIT_KEY, "防重提交"));
        CACHES.add(new RespSysCache(CacheConstant.RATE_LIMIT_KEY, "限流处理"));
        CACHES.add(new RespSysCache(CacheConstant.PWD_ERR_CNT_KEY, "密码错误次数"));
    }

    @Override
    public RespMonitorCache getInfo() throws ExecutionException, InterruptedException {

        RespMonitorCache result = new RespMonitorCache();

        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::info);
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        Long dbSize = (Long) redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        assert commandStats != null;
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StringUtils.removeStart(key, "cmdstat_"));
            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });

        result
                .setInfo(info)
                .setDbSize(dbSize)
                .setCommandStats(pieList);
        return result;
    }

    @Override
    public List<RespSysCache> cache() {

        return CACHES;
    }

    @Override
    public TreeSet<String> getCacheKeys(String cacheName) {

        Collection<String> cacheKeys = RedisUtil.keys(cacheName + "*");
        assert cacheKeys != null;
        return new TreeSet<>(cacheKeys);
    }

    @Override
    public RespSysCache getCacheValue(String cacheName, String cacheKey) {

        String cacheValue = RedisUtil.get(cacheKey);
        return new RespSysCache(cacheName, cacheKey, cacheValue);
    }

    @Override
    public Boolean clearCacheName(String cacheName) {

        Collection<String> cacheKeys = RedisUtil.keys(cacheName + "*");
        return RedisUtil.delete(cacheKeys);
    }

    @Override
    public Boolean clearCacheKey(String cacheKey) {

        return RedisUtil.delete(cacheKey);
    }

    @Override
    public Boolean clearCacheAll() {

        Collection<String> cacheKeys = RedisUtil.keys("*");
        assert cacheKeys != null;
        return RedisUtil.delete(cacheKeys);
    }
}