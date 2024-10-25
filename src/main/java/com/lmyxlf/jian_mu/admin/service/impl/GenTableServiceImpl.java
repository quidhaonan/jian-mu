package com.lmyxlf.jian_mu.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.admin.dao.GenTableDao;
import com.lmyxlf.jian_mu.admin.model.entity.GenTable;
import com.lmyxlf.jian_mu.admin.model.req.ReqGenTable;
import com.lmyxlf.jian_mu.admin.model.resp.RespGenTable;
import com.lmyxlf.jian_mu.admin.service.GenTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/24 0:33
 * @description
 * @since 17
 */
@Slf4j
@Service("genTableService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenTableServiceImpl extends ServiceImpl<GenTableDao, GenTable> implements GenTableService {

    @Override
    public Page<RespGenTable> genList(ReqGenTable reqGenTable) {

        return null;
    }
}