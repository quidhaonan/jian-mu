package com.lmyxlf.jian_mu.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.admin.dao.SysRoleDeptDao;
import com.lmyxlf.jian_mu.admin.model.entity.SysRoleDept;
import com.lmyxlf.jian_mu.admin.service.SysRoleDeptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/16 22:57
 * @description
 * @since 17
 */
@Slf4j
@Service("sysRoleDeptService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysRoleDeptServiceImpl extends ServiceImpl<SysRoleDeptDao, SysRoleDept> implements SysRoleDeptService {

}