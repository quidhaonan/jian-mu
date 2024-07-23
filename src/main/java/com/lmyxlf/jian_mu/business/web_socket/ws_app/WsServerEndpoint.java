package com.lmyxlf.jian_mu.business.web_socket.ws_app;

import cn.hutool.json.JSONUtil;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.WsBaseCmd;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.WsCmd;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.WsCmdType;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.handler.WsHandler;
import com.lmyxlf.jian_mu.global.constant.CODE_MSG;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/18 17:59
 * @description websocket 服务端
 * @since 17
 */
@Slf4j
@Component
@Scope("prototype")
@ServerEndpoint("/ws/connect")
public class WsServerEndpoint {


    public static Map<WsCmdType, WsHandler<? extends WsCmd>> handlerMap = new ConcurrentHashMap<>();

    /**
     * 连接成功
     */
    @OnOpen
    public void onOpen(Session session) {
        WsManager.openSession(session);
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose(Session session) {
        log.info("监听关闭 web session, sessionId：[{}]", session.getId());
        WsManager.closeSession(session);
    }

    /**
     * 连接异常
     */
    @OnError
    public void onError(Session session, Throwable t) {

        log.error("[ws 未知错误]，sessionId：{}，e：{}", session.getId(), t.getMessage());
        // t.printStackTrace();
    }

    /**
     * 接收到消息
     */
    @OnMessage()
    @Async("async_executor_ws")
    public void onMsg(Session session, String text) {

        // Pool.webExecutor.submit(() -> {
        try {
            // 序列化
            // WsBaseCmd wsBaseRes = ObjectUtils.readValue(text, WsBaseCmd.class);
            WsBaseCmd wsBaseRes = JSONUtil.toBean(text, WsBaseCmd.class);
            String reqType = wsBaseRes.getType();

            if (reqType == null) {
                WsManager.sendError(wsBaseRes, session, new LmyXlfException(CODE_MSG.ERROR));
                log.error("[ws web 接收数据异常], 类型不存在, sessionId：[{}]，text：[{}]", session.getId(), text);
                return;
            }

            WsCmdType type = WsCmdType.of(reqType);
            if (type == null) {
                WsManager.sendError(wsBaseRes, session, new LmyXlfException(CODE_MSG.ERROR));
                log.error("[ws web 接收数据异常],未知指令, sessionId：[{}]，type：[{}]，text：[{}]", session.getId(), reqType, text);
                return;
            }


            // 执行
            // WsCmd wsRes = ObjectUtils.convertValue(wsBaseRes.getData(), type.getResClazz());
            WsCmd wsRes = JSONUtil.toBean(JSONUtil.toJsonStr(wsBaseRes.getData()), type.getResClazz());
            try {
                WsHandler wsHandler = handlerMap.get(type);
                wsHandler.handle(wsRes, session, wsBaseRes);
                log.info("[ws web 接收处理消息]，type：[{}]，sessionId：[{}]，cmd：[{}]", type, session.getId(), wsBaseRes);
            } catch (Exception e) {
                WsManager.sendError(wsBaseRes, session, e);
                log.error("[ws web 数据处理异常]，type：[{}]，err：[{}]，sessionId：[{}]，cmd：[{}]", type, e.getMessage(), session.getId(), wsBaseRes);
            }

        } catch (Exception e) {
            // e.printStackTrace();
            log.error("[ws web 数据处理异常]，sessionId：[{}]，text：[{}]，e：[{}]", session.getId(), text, e.getMessage());
        }
        // });
    }

    /**
     * handler 注册
     *
     * @param type    指令类型
     * @param handler 执行方法
     */
    public static void registerHandler(WsCmdType type, WsHandler<? extends WsCmd> handler) {
        handlerMap.put(type, handler);
    }
}