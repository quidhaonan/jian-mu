package com.lmyxlf.jian_mu.admin.model.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/7 18:00
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
public class RespSysRole {

    /**
     * 主键 id
     */
    private Integer id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限字符串
     */
    private String roleKey;

    /**
     * 显示顺序
     */
    private Integer roleSort;

    /**
     * 数据范围，1：全部数据权限，2：自定数据权限，3：本部门数据权限，4：本部门及以下数据权限
     */
    private Integer dataScope;

    /**
     * 菜单树选择项是否关联显示，0：否，1：是
     */
    private Integer menuCheckStrictly;

    /**
     * 部门树选择项是否关联显示，0：否，1：是
     */
    private Integer deptCheckStrictly;

    /**
     * 角色状态，0：正常，1：停用
     */
    private Integer status;

    /**
     * 创建者
     */
    private String createUser;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateUser;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 用户是否存在此角色标识，默认不存在
     */
    private boolean flag = false;
}