package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.lmyxlf.jian_mu.admin.constant.AdminConstant;
import com.lmyxlf.jian_mu.admin.handler.PermissionContextHolder;
import com.lmyxlf.jian_mu.admin.model.dto.LoginUserDTO;
import com.lmyxlf.jian_mu.admin.model.dto.SysRoleDTO;
import com.lmyxlf.jian_mu.admin.service.PermissionService;
import com.lmyxlf.jian_mu.admin.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/31 1:18
 * @description 自定义权限实现
 * @since 17
 */
@Slf4j
@Service("permissionService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PermissionServiceImpl implements PermissionService {

    @Override
    public boolean hasPermi(String permission) {

        if (StrUtil.isEmpty(permission)) {

            return Boolean.FALSE;
        }
        LoginUserDTO loginUserDTO = SecurityUtil.getLoginUser();
        if (ObjUtil.isNull(loginUserDTO) || CollUtil.isEmpty(loginUserDTO.getPermissions())) {

            return Boolean.FALSE;
        }

        PermissionContextHolder.setContext(permission);

        return hasPermissions(loginUserDTO.getPermissions(), permission);
    }

    @Override
    public boolean lacksPermi(String permission) {

        return !hasPermi(permission);
    }

    @Override
    public boolean hasAnyPermi(String permissions) {

        if (StrUtil.isEmpty(permissions)) {

            return Boolean.FALSE;
        }
        LoginUserDTO loginUserDTO = SecurityUtil.getLoginUser();
        if (ObjUtil.isNull(loginUserDTO) || CollectionUtils.isEmpty(loginUserDTO.getPermissions())) {

            return Boolean.FALSE;
        }

        PermissionContextHolder.setContext(permissions);
        Set<String> authorities = loginUserDTO.getPermissions();

        for (String permission : permissions.split(AdminConstant.PERMISSION_DELIMETER)) {

            if (permission != null && hasPermissions(authorities, permission)) {

                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    @Override
    public boolean hasRole(String role) {

        if (StrUtil.isEmpty(role)) {

            return Boolean.FALSE;
        }
        LoginUserDTO loginUserDTO = SecurityUtil.getLoginUser();
        if (ObjUtil.isNull(loginUserDTO) || CollectionUtils.isEmpty(loginUserDTO.getPermissions())) {

            return Boolean.FALSE;
        }

        for (SysRoleDTO sysRoleDTO : loginUserDTO.getSysUserDTO().getSysRoleDTOS()) {

            String roleKey = sysRoleDTO.getRoleKey();
            if (AdminConstant.SUPER_ADMIN.equals(roleKey) || roleKey.equals(StrUtil.trim(role))) {

                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    @Override
    public boolean lacksRole(String role) {

        return !hasRole(role);
    }

    @Override
    public boolean hasAnyRoles(String roles) {

        if (StrUtil.isEmpty(roles)) {

            return Boolean.FALSE;
        }
        LoginUserDTO loginUserDTO = SecurityUtil.getLoginUser();
        if (ObjUtil.isNull(loginUserDTO) || CollectionUtils.isEmpty(loginUserDTO.getPermissions())) {

            return Boolean.FALSE;
        }

        for (String role : roles.split(AdminConstant.ROLE_DELIMETER)) {

            if (hasRole(role)) {

                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(Set<String> permissions, String permission) {

        return permissions.contains(AdminConstant.ALL_PERMISSION) ||
                permissions.contains(StrUtil.trim(permission));
    }
}