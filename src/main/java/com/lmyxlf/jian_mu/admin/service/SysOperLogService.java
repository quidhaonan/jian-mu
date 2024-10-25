package com.lmyxlf.jian_mu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lmyxlf.jian_mu.admin.model.entity.SysOperLog;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysOperLog;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysOperLog;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/14 0:15
 * @description
 * @since 17
 */
public interface SysOperLogService extends IService<SysOperLog> {

    Page<RespSysOperLog> list(ReqSysOperLog reqSysOperLog);

    void export(ReqSysOperLog reqSysOperLog, HttpServletResponse response);

    Boolean remove(ReqSysOperLog reqSysOperLog);

    Boolean clean();
}