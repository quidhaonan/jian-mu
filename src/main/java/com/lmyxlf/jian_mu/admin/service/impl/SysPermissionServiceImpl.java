package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.lmyxlf.jian_mu.admin.model.dto.SysRoleDTO;
import com.lmyxlf.jian_mu.admin.model.dto.SysUserDTO;
import com.lmyxlf.jian_mu.admin.model.entity.SysMenu;
import com.lmyxlf.jian_mu.admin.model.entity.SysRole;
import com.lmyxlf.jian_mu.admin.model.entity.SysRoleMenu;
import com.lmyxlf.jian_mu.admin.model.entity.SysUserRole;
import com.lmyxlf.jian_mu.admin.service.*;
import com.lmyxlf.jian_mu.global.constant.DBConstant;
import com.lmyxlf.jian_mu.global.util.SpringContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/12 23:55
 * @description
 * @since 17
 */
@Slf4j
@Service("sysPermissionService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysPermissionServiceImpl implements SysPermissionService {

    private final SysUserRoleService sysUserRoleService;
    private final SysRoleMenuService sysRoleMenuService;

    @Override
    public Set<String> getRolePermission(Integer userId) {

        Set<String> roles = new HashSet<>();
        // 管理员拥有所有权限
        if (SysUserDTO.isAdmin(userId)) {

            roles.add("admin");
        } else {

            List<SysUserRole> sysUserRoleList = sysUserRoleService.lambdaQuery()
                    .eq(SysUserRole::getUserId, userId)
                    .eq(SysUserRole::getDeleteTime, DBConstant.INITIAL_TIME)
                    .list();
            if (CollUtil.isEmpty(sysUserRoleList)) {

                log.warn("该用户无权限，userId：{}", userId);
                return roles;
            }

            List<Integer> roleIds = sysUserRoleList.stream().map(SysUserRole::getRoleId).toList();
            List<SysRole> sysRoleList = SpringContextHolder.getBean(SysRoleService.class).lambdaQuery()
                    .in(SysRole::getId, roleIds)
                    .eq(SysRole::getDeleteTime, DBConstant.INITIAL_TIME)
                    .list();

            sysRoleList.forEach(item -> {
                roles.addAll(Arrays.asList(item.getRoleKey().trim().split(",")));
            });
        }

        return roles;
    }

    @Override
    public Set<String> getMenuPermission(SysUserDTO sysUserDTO) {

        Set<String> perms = new HashSet<>();
        // 管理员拥有所有权限
        if (sysUserDTO.isAdmin()) {

            perms.add("*:*:*");
        } else {

            List<SysRoleDTO> roles = sysUserDTO.getSysRoleDTOS();
            if (!CollectionUtils.isEmpty(roles)) {

                // 多角色设置 permissions 属性，以便数据权限匹配权限
                for (SysRoleDTO roleDTO : roles) {

                    Set<String> rolePerms = selectMenuPermsByRoleId(Collections.singletonList(roleDTO.getId()));
                    roleDTO.setPermissions(rolePerms);
                    perms.addAll(rolePerms);
                }
            } else {

                perms.addAll(selectMenuPermsByUserId(sysUserDTO.getId()));
            }
        }

        return perms;
    }

    /**
     * 根据角色 id 查询权限
     *
     * @param roleIds 角色 id 集合
     * @return 权限列表
     */
    private Set<String> selectMenuPermsByRoleId(List<Integer> roleIds) {

        Set<String> result = new HashSet<>();

        if (CollUtil.isEmpty(roleIds)) {

            log.warn("用户 id 集合为空，权限列表为空，roleIds：{}", roleIds);
            return result;
        }
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.lambdaQuery()
                .in(SysRoleMenu::getRoleId, roleIds)
                .eq(SysRoleMenu::getDeleteTime, DBConstant.INITIAL_TIME)
                .list();
        if (CollUtil.isEmpty(sysRoleMenuList)) {

            log.warn("该用户无权限，roleIds：{}", roleIds);
            return result;
        }

        List<Integer> menuIds = sysRoleMenuList.stream().map(SysRoleMenu::getMenuId).toList();
        List<SysMenu> sysMenuList = SpringContextHolder.getBean(SysMenuService.class).lambdaQuery()
                .in(SysMenu::getId, menuIds)
                .eq(SysMenu::getDeleteTime, DBConstant.INITIAL_TIME)
                .list();
        sysMenuList.forEach(item -> {
            String itemPerm = item.getPerms();
            result.addAll(Arrays.asList(itemPerm.trim().split(",")));
        });

        return result;
    }

    /**
     * 根据用户 id 查询权限
     *
     * @param userId 用户 id
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByUserId(Integer userId) {

        List<SysUserRole> sysUserRoleList = sysUserRoleService.lambdaQuery()
                .eq(SysUserRole::getUserId, userId)
                .eq(SysUserRole::getDeleteTime, DBConstant.INITIAL_TIME)
                .list();
        if (CollUtil.isEmpty(sysUserRoleList)) {

            log.warn("该用户无权限，userId：{}", userId);
            return new HashSet<>();
        }

        List<Integer> roleIds = sysUserRoleList.stream().map(SysUserRole::getRoleId).toList();

        return selectMenuPermsByRoleId(roleIds);
    }
}