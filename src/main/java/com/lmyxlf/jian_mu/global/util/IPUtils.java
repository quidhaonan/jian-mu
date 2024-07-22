package com.lmyxlf.jian_mu.global.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.lmyxlf.jian_mu.global.constant.LmyXlfReqParamConstant;
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
     * 使用 Nginx 等反向代理软件， 则不能通过 request.getRemoteAddr() 获取 IP 地址
     * 如果使用了多级反向代理的话，X-Forwarded-For 的值并不止一个，而是一串 IP 地址，
     * X-Forwarded-For 中第一个非 unknown 的有效 IP 字符串，则为真实 IP 地址
     *
     * 如果使用 nginx 代理，则 X-Real-IP 为真实 ip，request.getRemoteAddr() 为 nginx 服务器 ip，X-Forwarded-For 伪装 ip
     * 会排在第一个，因此 X-Forwarded-For 中第一个非 unknown 的有效 IP 字符串，为用户伪装 ip
     * 如果不使用代理，那么 request.getRemoteAddr() 为真实 ip
     * </p>
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader(LmyXlfReqParamConstant.KEY_X_REAL_IP);

        // 各服务代理 ip
        if (ipAddress ==null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)){
            ipAddress=request.getHeader(LmyXlfReqParamConstant.KEY_PROXY_CLIENT_IP);
        }
        if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader(LmyXlfReqParamConstant.KEY_WL_PROXY_CLIENT_IP);
        }
        if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader(LmyXlfReqParamConstant.KEY_HTTP_CLIENT_IP);
        }

        if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (DEFAULT_IP_4_ADDRESS.equals(ipAddress) || DEFAULT_IP_6_ADDRESS.equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    // Monitor.error("get_local_host_error").log("获取本地host异常：{}", ExceptionUtil.getMessage(e)).inc();
                    log.error("获取本地host异常：{}", ExceptionUtil.getMessage(e));
                }
            }
        }

        // X-Forwarded-For ip，可能会存在伪装 ip
        if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader(LmyXlfReqParamConstant.KEY_X_FORWARDED_FOR);
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