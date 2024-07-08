package com.lmyxlf.jian_mu.global.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description 类 IPUtils 的功能描述
 * IP 地址
 * @since 17
 */
@Slf4j
public class IPUtils {

    public final static String UNKNOWN = "unknown";
    public final static String DEFAULT_IP_4_ADDRESS = "127.0.0.1";
    public final static String DEFAULT_IP_6_ADDRESS = "0:0:0:0:0:0:0:1";

    /**
     * 获取 IP 地址
     * <p>
     * 使用Nginx等反向代理软件， 则不能通过 request.getRemoteAddr() 获取 IP 地址
     * 如果使用了多级反向代理的话，X-Forwarded-For 的值并不止一个，而是一串 IP 地址，
     * X-Forwarded-For 中第一个非 unknown 的有效 IP 字符串，则为真实 IP 地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");

        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (DEFAULT_IP_4_ADDRESS.equals(ipAddress) || DEFAULT_IP_6_ADDRESS.equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    // Monitor.error("get_local_host_error").log("获取本地host异常：{}", ExceptionUtil.getMessage(e)).inc();
                    log.error("获取本地host异常：{}", ExceptionUtil.getMessage(e));
                }
                ipAddress = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个 IP 为客户端真实 IP,多个 IP 按照 ',' 分割 //"***.***.***.***".length() = 15
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
}