package com.lmyxlf.jian_mu.global.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description 使用该类要默认添加为 info 级别日志，避免引入太多日志
 *              查看建连时间、连接池情况再修改为 DEBUG 级别
 *              com.lmyxlf.jian_mu.global.util.HttpClientManager: INFO
 * @since 17
 */
@Slf4j
public class HttpClientManager extends PoolingHttpClientConnectionManager {

    @Override
    public void connect(
            final HttpClientConnection conn,
            final HttpRoute route,
            final int connectTimeout,
            final HttpContext context
    ) throws IOException {
        // 开启 debug 级别才记录时间
        if (log.isDebugEnabled()){
            long startTime = System.currentTimeMillis();
            try {
                super.connect(conn, route, connectTimeout, context);
            } finally {
                long endTime = System.currentTimeMillis();
                long connectTime = endTime - startTime;
                log.info("Connection to " + route.getTargetHost() + " took " + connectTime + " ms");
            }
            return;
        }
        super.connect(conn, route, connectTimeout, context);

    }
}