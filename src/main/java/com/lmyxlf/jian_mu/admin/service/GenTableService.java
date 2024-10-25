package com.lmyxlf.jian_mu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lmyxlf.jian_mu.admin.model.entity.GenTable;
import com.lmyxlf.jian_mu.admin.model.req.ReqGenTable;
import com.lmyxlf.jian_mu.admin.model.resp.RespGenTable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/24 0:32
 * @description
 * @since 17
 */
public interface GenTableService extends IService<GenTable> {

    Page<RespGenTable> genList(@RequestBody ReqGenTable reqGenTable);
}