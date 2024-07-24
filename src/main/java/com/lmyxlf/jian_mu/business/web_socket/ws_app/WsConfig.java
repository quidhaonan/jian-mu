package com.lmyxlf.jian_mu.business.web_socket.ws_app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/18 17:59
 * @description websocket 装填配置
 * @since 17
 */
@Configuration
@EnableWebSocket
public class WsConfig {

    @Bean
    public ServerEndpointExporter serverEndpoint() {
        return new ServerEndpointExporter();
    }
}