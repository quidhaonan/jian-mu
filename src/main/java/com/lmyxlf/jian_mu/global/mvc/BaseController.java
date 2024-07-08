package com.lmyxlf.jian_mu.global.mvc;


import com.lmyxlf.jian_mu.global.util.IPUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description 类的功能描述
 * 公共的控件器，可在类中实现一些共同的方法和属性
 * @since 17
 */
@Slf4j
public class BaseController {

    /**
     * 获取客户端的真实ip
     *
     * @param request
     * @return
     */
    protected String getClientIP(HttpServletRequest request) {
        return IPUtils.getIpAddr(request);
    }

    /**
     * 获取本机mac地址
     *
     * @param ia
     * @return
     * @throws SocketException
     */
    protected static String getLocalMac(InetAddress ia) throws SocketException {
        // 获取网卡，获取地址
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            // 字节转换为整数
            int temp = mac[i] & 0xff;
            String str = Integer.toHexString(temp);
            log.debug("每8位:{}", str);
            if (str.length() == 1) {
                sb.append("0").append(str);
            } else {
                sb.append(str);
            }
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 获取访问来源
     *
     * @param request
     * @return
     */
    protected static Integer getSource(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent").toLowerCase();
        if (userAgent.contains("micromessenger")) {
            // 微信
            return 4;
        } else if (userAgent.contains("android")) {
            // 安卓
            return 2;
        } else if (userAgent.contains("iphone") || userAgent.contains("ipad") || userAgent.contains("ipod")) {
            // 苹果
            return 3;
        } else {
            // 网页
            return 1;
        }
    }
}