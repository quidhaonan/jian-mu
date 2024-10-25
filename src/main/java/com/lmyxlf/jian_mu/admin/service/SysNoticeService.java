package com.lmyxlf.jian_mu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lmyxlf.jian_mu.admin.model.entity.SysNotice;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysNotice;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysNotice;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/30 12:37
 * @description
 * @since 17
 */
public interface SysNoticeService extends IService<SysNotice> {

    Page<RespSysNotice> list(ReqSysNotice reqSysNotice);

    RespSysNotice getInfo(Integer id);

    Boolean add(ReqSysNotice reqSysNotice);

    Boolean edit(ReqSysNotice reqSysNotice);

    Boolean remove(ReqSysNotice reqSysNotice);
}