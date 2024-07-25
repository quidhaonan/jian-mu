package com.lmyxlf.jian_mu.log;


import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 15:27
 * @description
 * @since 17
 */
public class PodLogConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {

        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "not_find";
        }
    }
}