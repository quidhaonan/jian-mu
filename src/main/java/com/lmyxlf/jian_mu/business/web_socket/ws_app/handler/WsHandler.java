package com.lmyxlf.jian_mu.business.web_socket.ws_app.handler;

import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.WsBaseCmd;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.WsCmd;

import javax.websocket.Session;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/18 19:06
 * @description websocket 处理继承接口
 * @since 17
 */
public interface WsHandler<T extends WsCmd>{

    Object handle(T cmd, Session session, WsBaseCmd wsWebBaseCmd);
}