package com.lmyxlf.jian_mu.business.web_socket.ws_app.handler.impl;

import com.lmyxlf.jian_mu.business.web_socket.constant.WSExceptionEnum;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.WsManager;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.WsServerEndpoint;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.WsStore;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.WsBaseCmd;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.WsCmdType;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.impl.InitCmd;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.handler.WsHandler;
import com.lmyxlf.jian_mu.global.constant.CODE_MSG;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.Session;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/19 15:20
 * @description 客户端初始化登录
 * @since 17
 */
@Slf4j
@Component
public class InitHandler implements WsHandler<InitCmd> {

    private static final WsCmdType CMD_TYPE = WsCmdType.INIT;

    @PostConstruct
    public void init() {
        WsServerEndpoint.registerHandler(CMD_TYPE, this);
    }

    @Override
    public Object handle(InitCmd cmd, Session session, WsBaseCmd wsBaseCmd) {

        // 1. 查询
        String clientId = cmd.getClientId();
        if (clientId == null) {
            log.error("[初始化 web 连接]，clientId 为空，sessionId：[{}]", session.getId());
            throw new LmyXlfException(CODE_MSG.ERROR);
        }

        // 2. 踢掉旧的连接
        // 判断 imei 旧的连接是否存在，sessionId 不一致就踢出
        WsStore store = WsManager.getStore(clientId);
        if (store != null && !session.getId().equals(store.getSession().getId())) {
            log.info("[初始化 web 连接]，踢出 session，clientId：[{}]，sessionId：[{}]", clientId, store.getSession().getId());
            WsManager.closeSession(store.getSession());
        }

        // 记录 ws 信息
        WsManager.initSession(session, clientId);

        WsManager.sendOK(session, wsBaseCmd, null);

        return null;
    }

    public static boolean AccessLegal(Session session, WsBaseCmd wsBaseCmd) {
        // 获取设备消息
        WsStore store = WsManager.getStoreBySessionId(session.getId());
        if (store == null) {
            WsManager.sendError(wsBaseCmd, session, new LmyXlfException(WSExceptionEnum.DEVICE_NOT_ONLINE.getMsg()));
            WsManager.closeSession(session);
            return false;
        }
        return true;
    }
}