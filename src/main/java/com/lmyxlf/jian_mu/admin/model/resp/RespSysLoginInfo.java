package com.lmyxlf.jian_mu.admin.model.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/8 2:27
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
public class RespSysLoginInfo {

    /**
     * 主键 id
     */
    private Integer id;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 登录 IP 地址
     */
    private String loginIp;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browserType;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录状态，0：成功，1：失败
     */
    private Integer status;

    /**
     * 提示消息
     */
    private String msg;

    /**
     * 访问时间
     */
    private LocalDateTime loginTime;
}