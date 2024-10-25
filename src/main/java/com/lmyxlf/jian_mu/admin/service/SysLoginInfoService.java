package com.lmyxlf.jian_mu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lmyxlf.jian_mu.admin.model.entity.SysLoginInfo;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysLoginInfo;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysLoginInfo;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/7 2:14
 * @description
 * @since 17
 */
public interface SysLoginInfoService extends IService<SysLoginInfo> {

    Page<RespSysLoginInfo> list(ReqSysLoginInfo reqSysLoginInfo);

    void export(ReqSysLoginInfo reqSysLoginInfo, HttpServletResponse response);

    Boolean remove(ReqSysLoginInfo reqSysLoginInfo);

    Boolean clean();
}