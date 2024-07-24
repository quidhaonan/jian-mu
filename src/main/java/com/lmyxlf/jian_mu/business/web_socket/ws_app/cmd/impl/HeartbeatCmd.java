package com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lmyxlf.jian_mu.business.web_socket.constant.WSParamEnum;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.WsCmd;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/18 19:06
 * @description ws 心跳请求体
 * @since 17
 */
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeartbeatCmd implements WsCmd {

    /**
     * 客户端ping，服务端pong
     */
    private String param = WSParamEnum.HEARTBEAT_PARM_STRING.getMsg();

}