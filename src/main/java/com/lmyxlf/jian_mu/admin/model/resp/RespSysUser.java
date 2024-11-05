package com.lmyxlf.jian_mu.admin.model.resp;

import com.lmyxlf.jian_mu.admin.model.entity.SysPost;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/17 0:00
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
public class RespSysUser {

    /**
     * 主键 id
     */
    private Integer id;

    /**
     * 部门 id
     */
    private Integer deptId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户类型，0：系统用户
     */
    private Integer userType;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 用户性别，0：男，1：女，2：未知
     */
    private Integer sex;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 密码
     */
    private String password;

    /**
     * 帐号状态，0：正常，1：停用
     */
    private String status;

    /**
     * 最后登录 ip
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

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
     * 部门名称
     */
    private String deptName;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 角色信息
     */
    private List<RespSysRole> roles;

    /**
     * 角色 id 集合
     */
    private List<Integer> roleIds;

    /**
     * 岗位信息
     */
    private List<SysPost> posts;

    /**
     * 岗位 id 集合
     */
    private List<Integer> postIds;
}