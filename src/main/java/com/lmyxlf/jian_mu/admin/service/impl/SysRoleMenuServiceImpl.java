package com.lmyxlf.jian_mu.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.admin.dao.SysRoleMenuDao;
import com.lmyxlf.jian_mu.admin.model.entity.SysRoleMenu;
import com.lmyxlf.jian_mu.admin.service.SysRoleMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/30 1:00
 * @description
 * @since 17
 */
@Slf4j
@Service("sysRoleMenuService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuDao, SysRoleMenu> implements SysRoleMenuService {

}