package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.admin.constant.CacheConstant;
import com.lmyxlf.jian_mu.admin.constant.UserConstant;
import com.lmyxlf.jian_mu.admin.dao.SysConfigDao;
import com.lmyxlf.jian_mu.admin.model.entity.SysConfig;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysConfig;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysConfig;
import com.lmyxlf.jian_mu.admin.service.SysConfigService;
import com.lmyxlf.jian_mu.admin.util.ExcelUtil;
import com.lmyxlf.jian_mu.global.constant.DBConstant;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import com.lmyxlf.jian_mu.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/29 20:11
 * @description
 * @since 17
 */
@Slf4j
@Service("sysConfigService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysConfigServiceImpl extends ServiceImpl<SysConfigDao, SysConfig> implements SysConfigService {

    /**
     * 验证码开关
     */
    private static final String CAPTCHA_ENABLED = "sys.account.captchaEnabled";

    @Override
    public Page<RespSysConfig> list(ReqSysConfig reqSysConfig) {

        Integer page = reqSysConfig.getPage();
        Integer size = reqSysConfig.getSize();
        String configName = reqSysConfig.getConfigName();
        Integer configType = reqSysConfig.getConfigType();
        String configKey = reqSysConfig.getConfigKey();
        LocalDateTime beginTime = reqSysConfig.getBeginTime();
        LocalDateTime endTime = reqSysConfig.getEndTime();

        LambdaQueryChainWrapper<SysConfig> sysConfigLambdaQueryChainWrapper = this.lambdaQuery()
                .eq(SysConfig::getDeleteTime, DBConstant.INITIAL_TIME)
                .like(StrUtil.isNotEmpty(configName), SysConfig::getConfigName, configName)
                .eq(ObjUtil.isNotNull(configType), SysConfig::getConfigType, configType)
                .like(StrUtil.isNotEmpty(configKey), SysConfig::getConfigKey, configKey)
                .ge(ObjUtil.isNotNull(beginTime), SysConfig::getCreateTime, beginTime)
                .le(ObjUtil.isNotNull(endTime), SysConfig::getCreateTime, endTime);

        Page<SysConfig> sysConfigPage = this.page(
                new Page<>(page, size), sysConfigLambdaQueryChainWrapper);


        // 仅为将返回对象转为 Resp
        List<RespSysConfig> records = sysConfigPage.getRecords().stream()
                .map(item -> {
                    RespSysConfig respSysConfig = new RespSysConfig();
                    BeanUtils.copyProperties(item, respSysConfig);
                    return respSysConfig;
                })
                .collect(Collectors.toList());
        Page<RespSysConfig> result = new Page<>(
                sysConfigPage.getCurrent(), sysConfigPage.getSize(), sysConfigPage.getTotal());
        result.setRecords(records);

        return result;
    }

    @Override
    public void export(ReqSysConfig reqSysConfig, HttpServletResponse response) {

        String configName = reqSysConfig.getConfigName();
        Integer configType = reqSysConfig.getConfigType();
        String configKey = reqSysConfig.getConfigKey();
        LocalDateTime beginTime = reqSysConfig.getBeginTime();
        LocalDateTime endTime = reqSysConfig.getEndTime();

        List<SysConfig> list = this.lambdaQuery()
                .eq(SysConfig::getDeleteTime, DBConstant.INITIAL_TIME)
                .like(StrUtil.isNotEmpty(configName), SysConfig::getConfigName, configName)
                .eq(ObjUtil.isNotNull(configType), SysConfig::getConfigType, configType)
                .like(StrUtil.isNotEmpty(configKey), SysConfig::getConfigKey, configKey)
                .ge(ObjUtil.isNotNull(beginTime), SysConfig::getCreateTime, beginTime)
                .le(ObjUtil.isNotNull(endTime), SysConfig::getCreateTime, endTime)
                .list();

        ExcelUtil<SysConfig> util = new ExcelUtil<>(SysConfig.class);
        util.exportExcel(response, list, "参数数据");
    }

    @Override
    public RespSysConfig selectConfigById(Integer id) {

        SysConfig sysConfig = this.lambdaQuery()
                .eq(SysConfig::getDeleteTime, DBConstant.INITIAL_TIME)
                .eq(SysConfig::getId, id)
                .one();

        RespSysConfig respSysConfig = new RespSysConfig();
        BeanUtils.copyProperties(sysConfig, respSysConfig);

        return respSysConfig;
    }

    @Override
    public Boolean add(ReqSysConfig reqSysConfig) {

        SysConfig sysConfig = new SysConfig();
        BeanUtils.copyProperties(reqSysConfig, sysConfig);

        boolean save = this.save(sysConfig);
        if (save) {

            RedisUtil.set(getCacheKey(sysConfig.getConfigKey()), sysConfig.getConfigValue());
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    @Override
    public Boolean edit(ReqSysConfig reqSysConfig) {

        Integer id = reqSysConfig.getId();
        String configKey = reqSysConfig.getConfigKey();

        SysConfig dbSysConfig = this.lambdaQuery()
                .eq(SysConfig::getId, id)
                .eq(SysConfig::getDeleteTime, DBConstant.INITIAL_TIME)
                .one();
        if (ObjUtil.isNull(dbSysConfig)) {

            log.warn("不存在此配置，reqSysConfig：{}", reqSysConfig);
            throw new LmyXlfException("不存在此配置");
        }

        BeanUtils.copyProperties(reqSysConfig, dbSysConfig);
        boolean flag = this.updateById(dbSysConfig);
        if (flag) {

            RedisUtil.delete(getCacheKey(configKey));
            RedisUtil.set(getCacheKey(reqSysConfig.getConfigKey()), reqSysConfig.getConfigValue());
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    @Override
    @Transactional
    public Boolean remove(ReqSysConfig reqSysConfig) {

        List<Integer> ids = reqSysConfig.getIds();

        List<SysConfig> list = this.lambdaQuery()
                .eq(SysConfig::getDeleteTime, DBConstant.INITIAL_TIME)
                .in(SysConfig::getId, ids)
                .list();
        list.forEach(item -> {
            if (UserConstant.YES.equals(item.getConfigType())) {

                log.warn("系统内置参数不能删除，reqSysConfig：{}，list：{}", reqSysConfig, list);
                throw new LmyXlfException("系统内置参数不能删除");
            }
            RedisUtil.delete(getCacheKey(item.getConfigKey()));
            item.setDeleteTime(LocalDateTimeUtil.now());
        });

        return this.updateBatchById(list);
    }

    @Override
    public Boolean refreshCache() {

        Collection<String> keys = RedisUtil.keys(CacheConstant.SYS_CONFIG_KEY + "*");
        RedisUtil.delete(keys);

        List<SysConfig> sysConfigs = this.lambdaQuery()
                .eq(SysConfig::getDeleteTime, DBConstant.INITIAL_TIME)
                .list();
        sysConfigs.forEach(item -> {
            RedisUtil.set(getCacheKey(item.getConfigKey()), item.getConfigValue());
        });

        return Boolean.TRUE;
    }

    @Override
    public Boolean selectCaptchaEnabled() {

        String captchaEnabled = selectConfigByKey(CAPTCHA_ENABLED);
        if (StrUtil.isEmpty(captchaEnabled)) {

            log.info("验证码开关返回默认值：true，captchaEnabled：{}", captchaEnabled);
            return Boolean.TRUE;
        }

        log.info("验证码开关：captchaEnabled：{}", captchaEnabled);
        return Convert.toBool(captchaEnabled);
    }

    @Override
    public String selectConfigByKey(String configKey) {

        String configValue = Convert.toStr(RedisUtil.get(getCacheKey(configKey)));
        if (StrUtil.isNotEmpty(configValue)) {

            return configValue;
        }

        SysConfig sysConfig = this.lambdaQuery()
                .eq(SysConfig::getConfigKey, configKey)
                .eq(SysConfig::getDeleteTime, DBConstant.INITIAL_TIME)
                .one();

        if (ObjUtil.isNotNull(sysConfig)) {

            RedisUtil.set(getCacheKey(configKey), sysConfig.getConfigValue());
            return sysConfig.getConfigValue();
        }
        return StrUtil.EMPTY;
    }

    /**
     * 设置 cache key
     *
     * @param configKey 参数键
     * @return 缓存键 key
     */
    private String getCacheKey(String configKey) {

        return CacheConstant.SYS_CONFIG_KEY + configKey;
    }

}