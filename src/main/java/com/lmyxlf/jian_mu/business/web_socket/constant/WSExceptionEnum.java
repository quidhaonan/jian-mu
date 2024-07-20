package com.lmyxlf.jian_mu.business.web_socket.constant;

import lombok.Getter;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/18 19:06
 * @description
 * @since 17
 */
@Getter
public enum WSExceptionEnum {
    DEVICE_NOT_ONLINE("1001", "设备不在线"),
    BROWSER_OFFLINE("1002", "浏览器已下线"),
    BROWSER_TIMEOUT("1003", "浏览器超时");

    private final String code;
    private final String msg;

    WSExceptionEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}