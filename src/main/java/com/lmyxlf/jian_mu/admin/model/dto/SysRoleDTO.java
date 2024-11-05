package com.lmyxlf.jian_mu.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import com.lmyxlf.jian_mu.admin.annotation.Excel;
import com.lmyxlf.jian_mu.admin.annotation.Excel.ColumnType;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/7 21:48
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@ApiModel("security 保存用户信息")
public class SysRoleDTO {

    @ApiModelProperty(value = "主键 id")
    @Excel(name = "角色序号", cellType = ColumnType.NUMERIC)
    private Integer id;

    @Excel(name = "角色名称")
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @Excel(name = "角色权限")
    @ApiModelProperty(value = "角色权限字符串")
    private String roleKey;

    @Excel(name = "角色排序")
    @ApiModelProperty(value = "显示顺序")
    private Integer roleSort;

    @ApiModelProperty(value = "数据范围，1：全部数据权限，2：自定数据权限，3：本部门数据权限，4：本部门及以下数据权限")
    @Excel(name = "数据范围", readConverterExp = "1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限")
    private Integer dataScope;

    @ApiModelProperty(value = "菜单树选择项是否关联显示，0：否，1：是")
    private Boolean menuCheckStrictly;

    @ApiModelProperty(value = "部门树选择项是否关联显示，0：否，1：是")
    private Boolean deptCheckStrictly;

    @ApiModelProperty(value = "角色状态，0：正常，1：停用")
    @Excel(name = "角色状态", readConverterExp = "0：正常，1：停用")
    private Integer status;

    @ApiModelProperty(value = "创建者")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新者")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除时间")
    private LocalDateTime deleteTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 用户是否存在此角色标识 默认不存在
     */
    private boolean flag = false;

    /**
     * 菜单组
     */
    private Long[] menuIds;

    /**
     * 部门组（数据权限）
     */
    private Long[] deptIds;

    /**
     * 角色菜单权限
     */
    private Set<String> permissions;

    public SysRoleDTO() {

    }

    public SysRoleDTO(Integer id) {

        this.id = id;
    }

    public boolean isAdmin() {

        return isAdmin(this.id);
    }

    public static boolean isAdmin(Integer roleId) {

        return roleId != null && 1 == roleId;
    }


    public boolean isMenuCheckStrictly() {

        return menuCheckStrictly;
    }

    public boolean isDeptCheckStrictly() {

        return deptCheckStrictly;
    }
}