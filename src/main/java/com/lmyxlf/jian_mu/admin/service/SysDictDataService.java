package com.lmyxlf.jian_mu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lmyxlf.jian_mu.admin.model.entity.SysDictData;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysDictData;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysDictData;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/20 22:47
 * @description
 * @since 17
 */
public interface SysDictDataService extends IService<SysDictData> {

    Page<RespSysDictData> list(ReqSysDictData reqSysDictData);

    void export(ReqSysDictData reqSysDictData, HttpServletResponse response);

    RespSysDictData getInfo(Integer id);

    List<RespSysDictData> dictType(String dictType);

    Boolean add(ReqSysDictData reqSysDictData);

    Boolean edit(ReqSysDictData reqSysDictData);

    Boolean remove(ReqSysDictData reqSysDictData);
}