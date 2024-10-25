package com.lmyxlf.jian_mu.admin.service;

import com.lmyxlf.jian_mu.admin.model.dto.SysUserDTO;

import java.util.Set;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/12 23:55
 * @description 用户权限处理
 * @since 17
 */
public interface SysPermissionService {

    /**
     * 获取角色数据权限
     *
     * @param userId 用户 id
     * @return 角色权限信息
     */
    Set<String> getRolePermission(Integer userId);

    /**
     * 获取菜单数据权限
     *
     * @param sysUserDTO 用户信息
     * @return 菜单权限信息
     */
    Set<String> getMenuPermission(SysUserDTO sysUserDTO);
}