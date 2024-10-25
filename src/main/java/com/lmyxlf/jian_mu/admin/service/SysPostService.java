package com.lmyxlf.jian_mu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lmyxlf.jian_mu.admin.model.entity.SysPost;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysPost;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysPost;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/30 18:47
 * @description
 * @since 17
 */
public interface SysPostService extends IService<SysPost> {

    Page<RespSysPost> list(ReqSysPost reqSysPost);

    void export(ReqSysPost reqSysPost, HttpServletResponse response);

    RespSysPost getInfo(Integer id);

    Boolean add(ReqSysPost reqSysPost);

    Boolean edit(ReqSysPost reqSysPost);

    Boolean remove(ReqSysPost reqSysPost);

    List<RespSysPost> optionselect();
}