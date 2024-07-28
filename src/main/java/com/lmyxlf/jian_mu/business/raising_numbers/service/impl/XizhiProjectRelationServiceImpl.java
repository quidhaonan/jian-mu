package com.lmyxlf.jian_mu.business.raising_numbers.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.business.raising_numbers.dao.XizhiProjectRelationDao;
import com.lmyxlf.jian_mu.business.raising_numbers.model.entity.XizhiProjectRelation;
import com.lmyxlf.jian_mu.business.raising_numbers.service.XizhiProjectRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/28 3:12
 * @description
 * @since 17
 */
@Service("xizhiProjectRelationService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class XizhiProjectRelationServiceImpl extends ServiceImpl<XizhiProjectRelationDao, XizhiProjectRelation> implements XizhiProjectRelationService {

}