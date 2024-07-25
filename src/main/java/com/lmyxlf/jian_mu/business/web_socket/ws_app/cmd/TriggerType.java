package com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/18 17:59
 * @description websocket 请求类型
 * @since 17
 */
@Getter
@AllArgsConstructor
public enum TriggerType {

    USER_SEND,
    EVENT
}