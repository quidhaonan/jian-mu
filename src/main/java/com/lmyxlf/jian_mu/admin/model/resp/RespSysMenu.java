package com.lmyxlf.jian_mu.admin.model.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/29 6:10
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
public class RespSysMenu {

    /**
     * 主键 id
     */
    private Integer id;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 父菜单 id
     */
    private Integer parentId;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
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
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 子菜单
     */
    private List<RespSysMenu> children = new ArrayList<>();

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