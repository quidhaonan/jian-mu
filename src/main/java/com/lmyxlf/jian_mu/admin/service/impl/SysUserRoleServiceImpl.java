package com.lmyxlf.jian_mu.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.admin.dao.SysUserRoleDao;
import com.lmyxlf.jian_mu.admin.model.entity.SysUserRole;
import com.lmyxlf.jian_mu.admin.service.SysUserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/1 2:25
 * @description
 * @since 17
 */
@Slf4j
@Service("sysUserRoleService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRole> implements SysUserRoleService {

}