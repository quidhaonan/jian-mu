package com.lmyxlf.jian_mu.business.web_socket.ws_app;

import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.WsCmdType;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.websocket.Session;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/18 17:59
 * @description websocket 客户端
 * @since 17
 */
@Data
@Accessors(chain = true)
public class WsStore {

    private String clientId;

    private Session session;

    private WsCmdType lastCmdType = null;
}