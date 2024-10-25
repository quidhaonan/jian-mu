package com.lmyxlf.jian_mu.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.admin.dao.GenTableColumnDao;
import com.lmyxlf.jian_mu.admin.model.entity.GenTableColumn;
import com.lmyxlf.jian_mu.admin.service.GenTableColumnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/24 0:40
 * @description
 * @since 17
 */
@Slf4j
@Service("genTableColumnService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenTableColumnServiceImpl extends ServiceImpl<GenTableColumnDao, GenTableColumn> implements GenTableColumnService {

}