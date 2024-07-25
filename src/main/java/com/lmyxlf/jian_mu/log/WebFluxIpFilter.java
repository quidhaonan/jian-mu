package com.lmyxlf.jian_mu.log;

import com.lmyxlf.jian_mu.global.constant.LmyXlfReqParamConstant;
import com.lmyxlf.jian_mu.global.util.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 15:27
 * @description
 * @since 17
 */
@Slf4j
@Configuration
public class WebFluxIpFilter implements WebFilter {

    public WebFluxIpFilter() {
        if (log.isDebugEnabled()) {
            log.debug("IP日志被创建");
        }
    }

    @Override
    public @NotNull Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        MDC.put(LmyXlfReqParamConstant.REMOTE_IP, IPUtils.getIpAddr(request));
        return chain.filter(exchange);
    }
}