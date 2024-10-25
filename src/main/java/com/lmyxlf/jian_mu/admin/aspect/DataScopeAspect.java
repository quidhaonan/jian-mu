package com.lmyxlf.jian_mu.admin.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.lmyxlf.jian_mu.admin.annotation.DataScope;
import com.lmyxlf.jian_mu.admin.handler.Convert;
import com.lmyxlf.jian_mu.admin.handler.PermissionContextHolder;
import com.lmyxlf.jian_mu.admin.model.dto.LoginUserDTO;
import com.lmyxlf.jian_mu.admin.model.dto.SysRoleDTO;
import com.lmyxlf.jian_mu.admin.model.dto.SysUserDTO;
import com.lmyxlf.jian_mu.admin.model.entity.DataScopeEntity;
import com.lmyxlf.jian_mu.admin.util.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/1 22:22
 * @description 数据过滤处理
 * @since 17
 */
@Aspect
@Component
public class DataScopeAspect {

    /**
     * 全部数据权限
     */
    public static final Integer DATA_SCOPE_ALL = 1;

    /**
     * 自定数据权限
     */
    public static final Integer DATA_SCOPE_CUSTOM = 2;

    /**
     * 部门数据权限
     */
    public static final Integer DATA_SCOPE_DEPT = 3;

    /**
     * 部门及以下数据权限
     */
    public static final Integer DATA_SCOPE_DEPT_AND_CHILD = 4;

    /**
     * 仅本人数据权限
     */
    public static final Integer DATA_SCOPE_SELF = 5;

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";

    @Before("@annotation(controllerDataScope)")
    public void doBefore(JoinPoint point, DataScope controllerDataScope) {

        clearDataScope(point);
        handleDataScope(point, controllerDataScope);
    }

    protected void handleDataScope(final JoinPoint joinPoint, DataScope controllerDataScope) {

        // 获取当前的用户
        LoginUserDTO loginUserDTO = SecurityUtil.getLoginUser();
        if (ObjUtil.isNotNull(loginUserDTO)) {

            SysUserDTO currentUser = loginUserDTO.getSysUserDTO();
            // 如果是超级管理员，则不过滤数据
            if (ObjUtil.isNotNull(currentUser) && !currentUser.isAdmin()) {

                String permission = StringUtils.defaultIfEmpty(controllerDataScope.permission(), PermissionContextHolder.getContext());
                dataScopeFilter(joinPoint, currentUser, controllerDataScope.deptAlias(),
                        controllerDataScope.userAlias(), permission);
            }
        }
    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint  切点
     * @param sysUserDTO 用户
     * @param deptAlias  部门别名
     * @param userAlias  用户别名
     * @param permission 权限字符
     */
    public static void dataScopeFilter(JoinPoint joinPoint, SysUserDTO sysUserDTO, String deptAlias, String userAlias, String permission) {

        StringBuilder sqlString = new StringBuilder();
        List<Integer> conditions = new ArrayList<>();
        List<String> scopeCustomIds = new ArrayList<>();
        sysUserDTO.getSysRoleDTOS().forEach(role -> {

            if (DATA_SCOPE_CUSTOM.equals(role.getDataScope()) && containsAny(role.getPermissions(), Convert.toStrArray(permission))) {

                scopeCustomIds.add(Convert.toStr(role.getId()));
            }
        });

        for (SysRoleDTO role : sysUserDTO.getSysRoleDTOS()) {

            Integer dataScope = role.getDataScope();
            if (conditions.contains(dataScope)) {

                continue;
            }
            if (!containsAny(role.getPermissions(), Convert.toStrArray(permission))) {

                continue;
            }
            if (DATA_SCOPE_ALL.equals(dataScope)) {

                sqlString = new StringBuilder();
                conditions.add(dataScope);
                break;
            } else if (DATA_SCOPE_CUSTOM.equals(dataScope)) {

                if (scopeCustomIds.size() > 1) {

                    // 多个自定数据权限使用in查询，避免多次拼接。
                    sqlString.append(StrUtil.format(" OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id in ({}) ) ", deptAlias, String.join(",", scopeCustomIds)));
                } else {

                    sqlString.append(StrUtil.format(" OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", deptAlias, role.getId()));
                }
            } else if (DATA_SCOPE_DEPT.equals(dataScope)) {

                sqlString.append(StrUtil.format(" OR {}.dept_id = {} ", deptAlias, sysUserDTO.getDeptId()));
            } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {

                sqlString.append(StrUtil.format(" OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )", deptAlias, sysUserDTO.getDeptId(), sysUserDTO.getDeptId()));
            } else if (DATA_SCOPE_SELF.equals(dataScope)) {

                if (StringUtils.isNotBlank(userAlias)) {

                    sqlString.append(StrUtil.format(" OR {}.user_id = {} ", userAlias, sysUserDTO.getId()));
                } else {

                    // 数据权限为仅本人且没有 userAlias 别名不查询任何数据
                    sqlString.append(StrUtil.format(" OR {}.dept_id = 0 ", deptAlias));
                }
            }

            conditions.add(dataScope);
        }

        // 角色都不包含传递过来的权限字符，这个时候 sqlString 也会为空，所以要限制一下,不查询任何数据
        if (CollUtil.isEmpty(conditions)) {

            sqlString.append(StrUtil.format(" OR {}.dept_id = 0 ", deptAlias));
        }

        if (StringUtils.isNotBlank(sqlString.toString())) {

            Object params = joinPoint.getArgs()[0];
            if (ObjUtil.isNotNull(params) && params instanceof DataScopeEntity dataScopeEntity) {

                dataScopeEntity.setDataScopeSql(" AND (" + sqlString.substring(4) + ")");
            }
        }
    }

    /**
     * 拼接权限 sql 前先清空 dataScopeSql 参数防止注入
     */
    private void clearDataScope(final JoinPoint joinPoint) {

        Object params = joinPoint.getArgs()[0];
        if (ObjUtil.isNotNull(params) && params instanceof DataScopeEntity dataScopeEntity) {

            dataScopeEntity.setDataScopeSql("");
        }
    }

    /**
     * 判断给定的 collection 列表中是否包含数组 array，判断给定的数组 array 中是否包含给定的元素 value
     *
     * @param collection 给定的集合
     * @param array      给定的数组
     * @return boolean 结果
     */
    private static boolean containsAny(Collection<String> collection, String... array) {

        if (CollUtil.isEmpty(collection) || ObjUtil.isNull(array) || array.length == 0) {

            return Boolean.FALSE;
        } else {

            for (String str : array) {

                if (collection.contains(str)) {

                    return Boolean.TRUE;
                }
            }

            return Boolean.FALSE;
        }
    }
}