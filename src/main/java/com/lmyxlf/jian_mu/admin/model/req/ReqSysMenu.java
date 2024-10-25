package com.lmyxlf.jian_mu.admin.model.req;

import com.lmyxlf.jian_mu.global.validation.group.Insert;
import com.lmyxlf.jian_mu.global.validation.group.Update;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/29 6:10
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
public class ReqSysMenu {

    /**
     * 主键 id
     */
    @Null(message = "主键 id 应为空", groups = {Insert.class})
    @NotNull(message = "主键 id 不能为空", groups = {Update.class})
    private Integer id;

    /**
     * 用户 id
     */
    private Integer userId;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空", groups = {Insert.class, Update.class})
    @Size(max = 50, message = "菜单名称长度不能超过50个字符", groups = {Insert.class, Update.class})
    private String menuName;

    /**
     * 父菜单 id
     */
    private Integer parentId;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空", groups = {Insert.class, Update.class})
    private Integer orderNum;

    /**
     * 路由地址
     */
    @Size(min = 0, max = 200, message = "路由地址不能超过200个字符", groups = {Insert.class, Update.class})
    private String path;

    /**
     * 组件路径
     */
    @Size(min = 0, max = 200, message = "组件路径不能超过255个字符", groups = {Insert.class, Update.class})
    private String component;

    /**
     * 路由参数
     */
    private String query;

    /**
     * 路由名称
     */
    private String routeName;

    /**
     * 是否为外链，0：是，1：否
     */
    private Integer isFrame;

    /**
     * 是否缓存，0：缓存，1：不缓存
     */
    private Integer isCache;

    /**
     * 菜单类型，0：目录，1：菜单，2：按钮
     */
    @NotBlank(message = "菜单类型不能为空", groups = {Insert.class, Update.class})
    private Integer menuType;

    /**
     * 菜单状态，0：显示，1：隐藏
     */
    private Integer visible;

    /**
     * 菜单状态，0：正常，1：停用
     */
    private Integer status;

    /**
     * 权限标识
     */
    @Size(min = 0, max = 100, message = "权限标识长度不能超过100个字符", groups = {Insert.class, Update.class})
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

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
}