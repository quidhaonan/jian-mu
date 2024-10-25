package com.lmyxlf.jian_mu.admin.model.req;

import com.lmyxlf.jian_mu.admin.annotation.Excel;
import com.lmyxlf.jian_mu.admin.annotation.Excel.Type;
import com.lmyxlf.jian_mu.admin.annotation.Excel.ColumnType;
import com.lmyxlf.jian_mu.admin.annotation.Excels;
import com.lmyxlf.jian_mu.admin.annotation.Xss;
import com.lmyxlf.jian_mu.admin.model.entity.SysDept;
import com.lmyxlf.jian_mu.admin.model.entity.SysRole;
import com.lmyxlf.jian_mu.global.model.PageEntity;
import com.lmyxlf.jian_mu.global.validation.group.Delete;
import com.lmyxlf.jian_mu.global.validation.group.Insert;
import com.lmyxlf.jian_mu.global.validation.group.Other;
import com.lmyxlf.jian_mu.global.validation.group.Update;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/7 2:30
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ReqSysUser extends PageEntity {

    /**
     * 主键 id
     */
    @Null(message = "主键 id 应为空", groups = {Insert.class})
    @NotNull(message = "主键 id 不能为空", groups = {Update.class})
    @Excel(name = "用户序号", type = Type.EXPORT, cellType = ColumnType.NUMERIC, prompt = "用户编号")
    private Integer id;

    /**
     * 主键 id 集合，配置 id
     */
    @NotEmpty(message = "主键 id 集合不能为空", groups = {Delete.class})
    private List<Integer> ids;

    /**
     * 部门 id
     */
    @Excel(name = "部门编号", type = Type.IMPORT)
    private Integer deptId;

    /**
     * 用户账号
     */
    @Excel(name = "登录名称")
    @Xss(message = "用户账号不能包含脚本字符")
    @Null(message = "用户账号不能更改", groups = {Update.class})
    @NotBlank(message = "用户账号不能为空", groups = {Insert.class})
    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符", groups = {Insert.class})
    private String userName;

    /**
     * 用户昵称
     */
    @Excel(name = "用户名称")
    @Xss(message = "用户昵称不能包含脚本字符")
    @Size(max = 30, message = "用户昵称长度不能超过30个字符", groups = {Insert.class, Update.class, Other.class})
    private String nickName;

    // @ApiModelProperty(value = "用户类型，0：系统用户")
    // private Integer userType;

    /**
     * 用户邮箱
     */
    @Excel(name = "用户邮箱")
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符", groups = {Insert.class, Update.class, Other.class})
    private String email;

    /**
     * 手机号码
     */
    @Excel(name = "手机号码", cellType = ColumnType.TEXT)
    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符", groups = {Insert.class, Update.class, Other.class})
    private String phoneNumber;

    /**
     * 用户性别，0：男，1：女，2：未知
     */
    @Excel(name = "用户性别", readConverterExp = "0：男，1：女，2：未知")
    private Integer sex;

    /**
     * 密码
     */
    private String password;

    /**
     * 帐号状态，0：正常，1：停用
     */
    @Excel(name = "帐号状态", readConverterExp = "0：正常，1：停用")
    private Integer status;

    /**
     * 最后登录 ip
     */
    @Excel(name = "最后登录 ip", type = Type.EXPORT)
    private String loginIp;

    /**
     * 最后登录时间
     */
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private LocalDateTime lastLoginTime;

    /**
     * 部门对象
     */
    @Excels({
            @Excel(name = "部门名称", targetAttr = "deptName", type = Type.EXPORT),
            @Excel(name = "部门负责人", targetAttr = "leader", type = Type.EXPORT)
    })
    private SysDept dept;

    /**
     * 角色对象
     */
    private List<SysRole> roles;

    /**
     * 角色组
     */
    private List<Integer> roleIds;

    /**
     * 岗位组
     */
    private List<Integer> postIds;

    /**
     * 角色 id
     */
    private Integer roleId;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}