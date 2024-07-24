package com.lmyxlf.jian_mu.business.raising_numbers.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.business.raising_numbers.dao.RaisingNumbersDao;
import com.lmyxlf.jian_mu.business.raising_numbers.model.entity.RaisingNumbers;
import com.lmyxlf.jian_mu.business.raising_numbers.service.RaisingNumbersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/24 14:16
 * @description
 * @since 17
 */
@Service("raisingNumbersService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RaisingNumbersServiceImpl extends ServiceImpl<RaisingNumbersDao, RaisingNumbers> implements RaisingNumbersService {

}