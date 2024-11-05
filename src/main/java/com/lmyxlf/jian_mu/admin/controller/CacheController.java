package com.lmyxlf.jian_mu.admin.controller;

import com.lmyxlf.jian_mu.admin.model.resp.RespMonitorCache;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysCache;
import com.lmyxlf.jian_mu.admin.service.CacheService;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/4 22:19
 * @description
 * @since 17
 */
@RestController
@Api(tags = "缓存监控")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.admin.urlPrefix}/monitor/cache")
public class CacheController {

    private final CacheService cacheService;

    @GetMapping()
    @PreAuthorize("@permissionService.hasPermi('monitor:cache:list')")
    public LmyXlfResult<RespMonitorCache> getInfo() throws ExecutionException, InterruptedException {

        return LmyXlfResult.ok(cacheService.getInfo());
    }

    @GetMapping("/getNames")
    @PreAuthorize("@permissionService.hasPermi('monitor:cache:list')")
    public LmyXlfResult<List<RespSysCache>> cache() {
        return LmyXlfResult.ok(cacheService.cache());
    }

    @GetMapping("/getKeys/{cacheName}")
    @PreAuthorize("@permissionService.hasPermi('monitor:cache:list')")
    public LmyXlfResult<TreeSet<String>> getCacheKeys(@PathVariable String cacheName) {

        return LmyXlfResult.ok(cacheService.getCacheKeys(cacheName));
    }

    @GetMapping("/getValue/{cacheName}/{cacheKey}")
    @PreAuthorize("@permissionService.hasPermi('monitor:cache:list')")
    public LmyXlfResult<RespSysCache> getCacheValue(@PathVariable String cacheName, @PathVariable String cacheKey) {

        return LmyXlfResult.ok(cacheService.getCacheValue(cacheName, cacheKey));
    }

    @DeleteMapping("/clearCacheName/{cacheName}")
    @PreAuthorize("@permissionService.hasPermi('monitor:cache:list')")
    public LmyXlfResult<Boolean> clearCacheName(@PathVariable String cacheName) {

        return LmyXlfResult.ok(cacheService.clearCacheName(cacheName));
    }

    @DeleteMapping("/clearCacheKey/{cacheKey}")
    @PreAuthorize("@permissionService.hasPermi('monitor:cache:list')")
    public LmyXlfResult<Boolean> clearCacheKey(@PathVariable String cacheKey) {

        return LmyXlfResult.ok(cacheService.clearCacheKey(cacheKey));
    }

    @DeleteMapping("/clearCacheAll")
    @PreAuthorize("@permissionService.hasPermi('monitor:cache:list')")
    public LmyXlfResult<Boolean> clearCacheAll() {

        return LmyXlfResult.ok(cacheService.clearCacheAll());
    }
}