package com.lmyxlf.jian_mu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lmyxlf.jian_mu.admin.model.entity.SysConfig;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysConfig;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysConfig;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/28 21:16
 * @description
 * @since 17
 */
public interface SysConfigService extends IService<SysConfig> {

    Page<RespSysConfig> list(ReqSysConfig reqSysConfig);

    void export(ReqSysConfig reqSysConfig, HttpServletResponse response);

    RespSysConfig selectConfigById(Integer id);

    Boolean add( ReqSysConfig reqSysConfig);

    Boolean edit(ReqSysConfig reqSysConfig);

    Boolean remove( ReqSysConfig reqSysConfig);

    Boolean refreshCache();

    /**
     * 获取验证码开关
     *
     * @return true：开启，false：关闭
     */
    Boolean selectCaptchaEnabled();

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
     String selectConfigByKey(String configKey);
}