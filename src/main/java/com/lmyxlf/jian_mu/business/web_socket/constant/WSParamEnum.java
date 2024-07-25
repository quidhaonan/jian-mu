package com.lmyxlf.jian_mu.business.web_socket.constant;

import lombok.Getter;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/18 19:18
 * @description ws 枚举类
 * @since 17
 */
@Getter
public enum WSParamEnum {
    HEARTBEAT_PARM_STRING(1, "heartbeat_parm_string"),
    HEARTBEAT_WORD(2, "heartbeat_word");


    private final Integer code;
    private final String msg;

    WSParamEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}