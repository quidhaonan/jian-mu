package com.lmyxlf.jian_mu.log;

import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.IThrowableProxy;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 15:27
 * @description
 * @since 17
 */
public class LineBreakThrowableProxyConverter extends ThrowableProxyConverter {

    @Override
    protected String throwableProxyToString(IThrowableProxy tp) {
        return super.throwableProxyToString(tp)
                .replaceAll("\n"," ")
                .replaceAll("\r"," ")
                .replaceAll("\t"," ")
                .replaceAll("\"","'");
    }
}
