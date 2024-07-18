package com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd;

import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.impl.HeartbeatCmd;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.impl.InitCmd;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/18 17:59
 * @description websocket 请求注册枚举
 * @since 17
 */
@Getter
@AllArgsConstructor
public enum WsCmdType {

    /**
     * isRequest：暂时无用字段，可取任意 Boolean
     */

    PING("heartbeat", HeartbeatCmd.class, true),
    INIT("init", InitCmd.class, true),
    ;

    private final String type;

    private final Class<? extends WsCmd> resClazz;

    private final boolean isRequest;

    public static WsCmdType of(String textType) {
        if (textType == null) {
            return null;
        }

        for (WsCmdType value : values()) {
            if (value.type.equals(textType)) {
                return value;
            }
        }

        return null;
    }
}