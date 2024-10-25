package com.lmyxlf.jian_mu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lmyxlf.jian_mu.admin.model.entity.SysDictType;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysDictType;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysDictType;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/20 23:15
 * @description
 * @since 17
 */
public interface SysDictTypeService extends IService<SysDictType> {

    Page<RespSysDictType> list(ReqSysDictType reqSysDictType);

    void export(ReqSysDictType reqSysDictType, HttpServletResponse response);

    RespSysDictType getInfo(Integer id);

    Boolean add(ReqSysDictType reqSysDictType);

    Boolean edit(ReqSysDictType reqSysDictType);

    Boolean remove(ReqSysDictType reqSysDictType);

    Boolean refreshCache();

    List<RespSysDictType> optionselect();
}