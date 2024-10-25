package com.lmyxlf.jian_mu.admin.constant;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/17 1:37
 * @description 用户常量信息
 * @since 17
 */
public class UserConstant {

    /**
     * 平台内系统用户的唯一标志
     */
    public static final String SYS_USER = "SYS_USER";

    /**
     * 正常状态
     */
    public static final Integer NORMAL = 0;

    /**
     * 停用状态
     */
    public static final Integer DISABLE = 1;

    /**
     * 异常状态
     */
    public static final Integer EXCEPTION = 1;

    /**
     * 用户封禁状态
     */
    public static final String USER_DISABLE = "1";

    /**
     * 角色封禁状态
     */
    public static final String ROLE_DISABLE = "1";

    /**
     * 字典正常状态
     */
    public static final String DICT_NORMAL = "0";

    /**
     * 是否为系统默认（是）
     */
    public static final Integer YES = 1;

    /**
     * 是否菜单外链（是）
     */
    public static final Integer YES_FRAME = 0;

    /**
     * 是否菜单外链（否）
     */
    public static final Integer NO_FRAME = 1;

    /**
     * Layout组件标识
     */
    public final static String LAYOUT = "Layout";

    /**
     * ParentView组件标识
     */
    public final static String PARENT_VIEW = "ParentView";

    /**
     * InnerLink组件标识
     */
    public final static String INNER_LINK = "InnerLink";

    /**
     * 校验是否唯一的返回标识
     */
    public final static boolean UNIQUE = true;
    public final static boolean NOT_UNIQUE = false;

    /**
     * 用户名长度限制
     */
    public static final int USERNAME_MIN_LENGTH = 2;
    public static final int USERNAME_MAX_LENGTH = 20;

    /**
     * 密码长度限制
     */
    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 20;

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";
}