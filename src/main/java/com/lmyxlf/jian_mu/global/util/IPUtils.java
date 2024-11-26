package com.lmyxlf.jian_mu.global.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjUtil;
import com.lmyxlf.jian_mu.global.constant.CODE_MSG;
import com.lmyxlf.jian_mu.global.constant.LmyXlfReqParamConstant;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

    public static final String UNKNOWN = "unknown";
    public static final String DEFAULT_IP_4_ADDRESS = "127.0.0.1";
    public static final String DEFAULT_IP_6_ADDRESS = "0:0:0:0:0:0:0:1";
    public static final Integer IP_LEN = 15;
    public static final String NETWORK_CARD = "网卡";
    /**
     * ip2region.xdb 路径
     */
    public static final String IP2REGION_XDB_PATH = "global/ip2region.xdb";

    /**
     * 根据 IP 获得地区
     * 备注：并发使用，每个线程需要创建一个独立的 searcher 对象单独使用
     *
     * @param ip
     * @return
     */
    public static String getAddressByIp(String ip) {

        if (!Validator.isIpv4(ip) && !Validator.isIpv6(ip)) {

            log.error("根据 ip 获得地区失败，ip：{}", ip);
            throw new LmyXlfException(CODE_MSG.IP_ERROR);
        }
        // 创建 searcher 对象
        String path = Objects.requireNonNull(
                IPUtils.class.getClassLoader().getResource(IP2REGION_XDB_PATH)).getPath();
        Searcher searcher = null;
        try {

            searcher = Searcher.newWithFileOnly(path);

            // 查询
            long startTime = System.nanoTime();
            String region = searcher.search(ip);
            long costTime = TimeUnit.NANOSECONDS.toMicros((long) (System.nanoTime() - startTime));

            log.info("根据 ip 成功获得地区，ip：{}，region：{}，ioCount：{}，花费时长：{}",
                    ip, region, searcher.getIOCount(), costTime);
            return region;
        } catch (Exception e) {

            log.error("根据 ip 获得地区失败，ip：{}，path：{}，e：{}", ip, path, e.getMessage());
            return UNKNOWN;
        } finally {
            // 关闭资源
            if (ObjUtil.isNotNull(searcher)) {

                try {

                    searcher.close();
                } catch (IOException e) {

                    log.error("根据 ip 获得地区，资源关闭失败，ip：{}，path：{}，e：{}", ip, path, e.getMessage());
                }
            }
        }
    }

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
    public static String getIpAddr() {

        Optional<HttpServletRequest> request = SpringMvcUtil.getRequest();
        return getIpAddr(request.orElseThrow(() -> new IllegalStateException("Request not present")));
    }

    public static String getIpAddr(HttpServletRequest request) {

        String ipAddress = request.getHeader(LmyXlfReqParamConstant.KEY_X_REAL_IP);
        String header = LmyXlfReqParamConstant.KEY_X_REAL_IP;

        // 各服务代理 ip
        if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {

            ipAddress = request.getHeader(LmyXlfReqParamConstant.KEY_PROXY_CLIENT_IP);
            header = LmyXlfReqParamConstant.KEY_PROXY_CLIENT_IP;
        }
        if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {

            ipAddress = request.getHeader(LmyXlfReqParamConstant.KEY_WL_PROXY_CLIENT_IP);
            header = LmyXlfReqParamConstant.KEY_WL_PROXY_CLIENT_IP;
        }
        if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {

            ipAddress = request.getHeader(LmyXlfReqParamConstant.KEY_HTTP_CLIENT_IP);
            header = LmyXlfReqParamConstant.KEY_HTTP_CLIENT_IP;
        }

        if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {

            ipAddress = request.getRemoteAddr();
            if (DEFAULT_IP_4_ADDRESS.equals(ipAddress) || DEFAULT_IP_6_ADDRESS.equals(ipAddress)) {

                // 根据网卡取本机配置的 ip
                try {

                    InetAddress inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (UnknownHostException e) {

                    // Monitor.error("get_local_host_error").log("获取本地host异常：{}", ExceptionUtil.getMessage(e)).inc();
                    log.error("获取本地 host 异常：{}", ExceptionUtil.getMessage(e));
                }
            }
            header = NETWORK_CARD;
        }

        // X-Forwarded-For ip，可能会存在伪装 ip
        if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {

            ipAddress = request.getHeader(LmyXlfReqParamConstant.KEY_X_FORWARDED_FOR);
            header = LmyXlfReqParamConstant.KEY_X_FORWARDED_FOR;
        }

        // 对于通过多个代理的情况，第一个 IP 为客户端真实 IP,多个 IP 按照 ',' 分割 //"***.***.***.***".length() = 15
        if (ipAddress != null && ipAddress.length() > IP_LEN) {

            log.info("存在伪装 ip，header：{}，ip：{}", header, ipAddress);
            if (ipAddress.indexOf(",") > 0) {

                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }

        log.info("从{}中取得 ip，ip 地址为{}", header, ipAddress);
        return ipAddress;
    }

    public static String getIpAddr(ServerHttpRequest request) {

        HttpHeaders headers = request.getHeaders();
        String ipAddress = headers.getFirst(LmyXlfReqParamConstant.KEY_X_REAL_IP);
        String header = LmyXlfReqParamConstant.KEY_X_REAL_IP;

        // 各服务代理 ip
        if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {

            ipAddress = headers.getFirst(LmyXlfReqParamConstant.KEY_PROXY_CLIENT_IP);
            header = LmyXlfReqParamConstant.KEY_PROXY_CLIENT_IP;
        }
        if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {

            ipAddress = headers.getFirst(LmyXlfReqParamConstant.KEY_WL_PROXY_CLIENT_IP);
            header = LmyXlfReqParamConstant.KEY_WL_PROXY_CLIENT_IP;
        }
        if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {

            ipAddress = headers.getFirst(LmyXlfReqParamConstant.KEY_HTTP_CLIENT_IP);
            header = LmyXlfReqParamConstant.KEY_HTTP_CLIENT_IP;
        }

        if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {

            ipAddress = Optional.ofNullable(request.getRemoteAddress())
                    .map(address -> address.getAddress().getHostAddress())
                    .orElse("");
            if (DEFAULT_IP_4_ADDRESS.equals(ipAddress) || DEFAULT_IP_6_ADDRESS.equals(ipAddress)) {

                // 根据网卡取本机配置的 ip
                try {

                    InetAddress inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (UnknownHostException e) {

                    // ignore
                    log.error("获取本地 host 异常：{}", ExceptionUtil.getMessage(e));
                }
            }
            header = NETWORK_CARD;
        }

        // X-Forwarded-For ip，可能会存在伪装 ip
        if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN.equalsIgnoreCase(ipAddress)) {

            ipAddress = headers.getFirst(LmyXlfReqParamConstant.KEY_X_FORWARDED_FOR);
            header = LmyXlfReqParamConstant.KEY_X_FORWARDED_FOR;
        }

        // 对于通过多个代理的情况，第一个 IP 为客户端真实 IP,多个 IP 按照 ',' 分割 //"***.***.***.***".length() = 15
        if (ipAddress != null && ipAddress.length() > IP_LEN) {

            log.info("存在伪装 ip，header：{}，ip：{}", header, ipAddress);
            int index = ipAddress.indexOf(",");
            if (index > 0) {

                ipAddress = ipAddress.substring(0, index);
            }
        }

        log.info("从{}中取得 ip，ip 地址为{}", header, ipAddress);
        return ipAddress;
    }

    /**
     * 获得本地 IP
     *
     * @return
     */
    public static String getLocalIp() {

        try {

            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {

            return DEFAULT_IP_4_ADDRESS;
        }
    }

    /**
     * 获得本地主机名
     *
     * @return
     */
    public static String getLocalHostName() {

        try {

            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {

            return UNKNOWN;
        }
    }
}