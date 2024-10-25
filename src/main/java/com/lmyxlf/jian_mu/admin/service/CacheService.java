package com.lmyxlf.jian_mu.admin.service;

import com.lmyxlf.jian_mu.admin.model.resp.RespMonitorCache;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysCache;

import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/4 22:55
 * @description
 * @since 17
 */
public interface CacheService {

    RespMonitorCache getInfo() throws ExecutionException, InterruptedException;

    List<RespSysCache> cache();

    TreeSet<String> getCacheKeys(String cacheName);

    RespSysCache getCacheValue(String cacheName, String cacheKey);

    Boolean clearCacheName(String cacheName);

    Boolean clearCacheKey(String cacheKey);

    Boolean clearCacheAll();
}