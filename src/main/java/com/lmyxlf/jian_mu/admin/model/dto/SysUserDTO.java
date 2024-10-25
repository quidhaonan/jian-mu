package com.lmyxlf.jian_mu.admin.model.dto;

import com.lmyxlf.jian_mu.admin.annotation.Excel;
import com.lmyxlf.jian_mu.admin.annotation.Excel.ColumnType;
import com.lmyxlf.jian_mu.admin.annotation.Excel.Type;
import com.lmyxlf.jian_mu.admin.annotation.Excels;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/7 18:30
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@ApiModel("security 保存用户信息")
public class SysUserDTO {

    @ApiModelProperty(value = "主键 id，即用户 id")
    @Excel(name = "用户序号", type = Type.EXPORT, cellType = ColumnType.NUMERIC, prompt = "用户编号")
    private Integer id;

    @ApiModelProperty(value = "部门 id")
    @Excel(name = "部门编号", type = Type.IMPORT)
    private Integer deptId;

    @Excel(name = "登录名称")
    @ApiModelProperty(value = "用户账号")
    private String userName;

    @Excel(name = "用户名称")
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户类型，0：系统用户")
    private Integer userType;

    @Excel(name = "用户邮箱")
    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    @Excel(name = "手机号码", cellType = ColumnType.TEXT)
    private String phoneNumber;

    @ApiModelProperty(value = "用户性别，0：男，1：女，2：未知")
    @Excel(name = "用户性别", readConverterExp = "0：男，1：女，2：未知")
    private Integer sex;

    @ApiModelProperty(value = "头像地址")
    private String avatar;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "帐号状态，0：正常，1：停用")
    @Excel(name = "帐号状态", readConverterExp = "0：正常，1：停用")
    private Integer status;

    @ApiModelProperty(value = "最后登录 ip")
    @Excel(name = "最后登录 ip", type = Type.EXPORT)
    private String loginIp;

    @ApiModelProperty(value = "最后登录时间")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private LocalDateTime lastLoginTime;

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
     * 部门对象
     */
    @Excels({
            @Excel(name = "部门名称", targetAttr = "deptName", type = Type.EXPORT),
            @Excel(name = "部门负责人", targetAttr = "leader", type = Type.EXPORT)
    })
    private SysDeptDTO sysDeptDTO;

    /**
     * 角色对象
     */
    private List<SysRoleDTO> sysRoleDTOS;

    /**
     * 角色组
     */
    private Integer[] roleIds;

    /**
     * 岗位组
     */
    private Integer[] postIds;

    /**
     * 角色ID
     */
    private Integer roleId;

    public SysUserDTO() {

    }

    public SysUserDTO(Integer id) {

        this.id = id;
    }

    public boolean isAdmin() {

        return isAdmin(this.id);
    }

    public static boolean isAdmin(Integer userId) {

        return userId != null && 1 == userId;
    }
}