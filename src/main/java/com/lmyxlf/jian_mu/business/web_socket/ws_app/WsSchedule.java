package com.lmyxlf.jian_mu.business.web_socket.ws_app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/18 17:59
 * @description websocket 监控定时任务
 * @since 17
 */
@Slf4j
@Component
public class WsSchedule {

    /**
     * 活跃设备监测
     */
    @Scheduled(fixedRate = 20 * 1000)
    public void checkWsActive() {
        WsManager.buildActiveCache();
        int size = WsManager.getAllActiveWebs().size();
        if (size > 0) {
            log.info("活跃 web：{}", String.join(",", WsManager.getAllActiveWebs()));
        }
    }

    /**
     * 活跃 session 连接
     */
    @Scheduled(fixedRate = 60 * 1000)
    public void checkConnectedSession() {
        Set<String> sessionIds = WsManager.checkConnectedSession();
        if (!sessionIds.isEmpty()) {
            log.info("ws 连接：{}", String.join(",", sessionIds));
        }

        long size = WsManager.getAllWebStores().size();
        if (size > 0) {
            log.info("clientId 和 session 关联缓存大小是:{}", size);
        }
    }
}