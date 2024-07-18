package com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.WsCmd;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/18 19:06
 * @description 客户端注册请求体
 * @since 17
 */
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InitCmd implements WsCmd {

    private String clientId;
}