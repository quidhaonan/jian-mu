package com.lmyxlf.jian_mu.business.web_socket.ws_app.handler.impl;

import com.lmyxlf.jian_mu.business.web_socket.constant.WSParamEnum;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.WsManager;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.WsServerEndpoint;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.WsBaseCmd;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.WsCmdType;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.impl.HeartbeatCmd;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.handler.WsHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.Session;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/19 15:20
 * @description 心跳处理
 * @since 17
 */
@Slf4j
@Component
public class HeartbeatHandler implements WsHandler<HeartbeatCmd> {

    private static final WsCmdType CMD_TYPE = WsCmdType.PING;

    @PostConstruct
    public void init() {
        WsServerEndpoint.registerHandler(CMD_TYPE, this);
    }

    @Override
    public Object handle(HeartbeatCmd cmd, Session session, WsBaseCmd wsWebBaseCmd) {


        // 缓存续期
        boolean equals = cmd.getParam().equals(WSParamEnum.HEARTBEAT_PARM_STRING.getMsg());
        if (equals) {
            boolean exist = WsManager.renewCache(session);

            cmd.setParam(WSParamEnum.HEARTBEAT_WORD.getMsg());

            if (exist) {
                WsManager.sendOK(session, wsWebBaseCmd, cmd);
            }
        }

        return null;
    }
}