package com.lmyxlf.jian_mu.admin.util;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/23 23:31
 * @description IP 方法
 * @since 17
 */
public class IPUtil {

    public final static String REGX_0_255 = "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
    // 匹配 ip
    public final static String REGX_IP = "((" + REGX_0_255 + "\\.){3}" + REGX_0_255 + ")";
    public final static String REGX_IP_WILDCARD = "(((\\*\\.){3}\\*)|(" + REGX_0_255 + "(\\.\\*){3})|(" + REGX_0_255 + "\\." + REGX_0_255 + ")(\\.\\*){2}" + "|((" + REGX_0_255 + "\\.){3}\\*))";
    // 匹配网段
    public final static String REGX_IP_SEG = "(" + REGX_IP + "\\-" + REGX_IP + ")";

    /**
     * 检查是否为内部 ip 地址
     *
     * @param ip ip 地址
     * @return 结果
     */
    public static boolean internalIp(String ip) {

        byte[] addr = textToNumericFormatV4(ip);
        return internalIp(addr) || "127.0.0.1".equals(ip);
    }

    /**
     * 检查是否为内部 ip 地址
     *
     * @param addr byte 地址
     * @return 结果
     */
    private static boolean internalIp(byte[] addr) {

        if (ObjUtil.isNull(addr) || addr.length < 2) {

            return true;
        }
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        // 10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        // 172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        // 192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        switch (b0) {

            case SECTION_1:
                return true;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {

                    return true;
                }
            case SECTION_5:
                switch (b1) {
                    case SECTION_6:
                        return true;
                }
            default:
                return false;
        }
    }

    /**
     * 将 IPv4 地址转换成字节
     *
     * @param text IPv4 地址
     * @return byte 字节
     */
    public static byte[] textToNumericFormatV4(String text) {

        if (StrUtil.isEmpty(text)) {

            return null;
        }

        byte[] bytes = new byte[4];
        String[] elements = text.split("\\.", -1);
        try {

            long l;
            int i;
            switch (elements.length) {

                case 1:
                    l = Long.parseLong(elements[0]);
                    if ((l < 0L) || (l > 4294967295L)) {
                        return null;
                    }
                    bytes[0] = (byte) (int) (l >> 24 & 0xFF);
                    bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 2:
                    l = Integer.parseInt(elements[0]);
                    if ((l < 0L) || (l > 255L)) {
                        return null;
                    }
                    bytes[0] = (byte) (int) (l & 0xFF);
                    l = Integer.parseInt(elements[1]);
                    if ((l < 0L) || (l > 16777215L)) {
                        return null;
                    }
                    bytes[1] = (byte) (int) (l >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 3:
                    for (i = 0; i < 2; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    l = Integer.parseInt(elements[2]);
                    if ((l < 0L) || (l > 65535L)) {
                        return null;
                    }
                    bytes[2] = (byte) (int) (l >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 4:
                    for (i = 0; i < 4; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    break;
                default:
                    return null;
            }
        } catch (NumberFormatException e) {

            return null;
        }

        return bytes;
    }

    /**
     * 从多级反向代理中获得第一个非 unknown ip 地址
     *
     * @param ip 获得的 ip 地址
     * @return 第一个非 unknown ip 地址
     */
    public static String getMultistageReverseProxyIp(String ip) {

        // 多级反向代理检测
        if (ip != null && ip.indexOf(",") > 0) {

            final String[] ips = ip.trim().split(",");
            for (String subIp : ips) {

                if (!isUnknown(subIp)) {

                    ip = subIp;
                    break;
                }
            }
        }

        return StringUtils.substring(ip, 0, 255);
    }

    /**
     * 检测给定字符串是否为未知，多用于检测 http 请求相关
     *
     * @param checkString 被检测的字符串
     * @return 是否未知
     */
    public static boolean isUnknown(String checkString) {

        return StringUtils.isBlank(checkString) || "unknown".equalsIgnoreCase(checkString);
    }

    /**
     * 是否为 ip
     */
    public static boolean isIP(String ip) {

        return StringUtils.isNotBlank(ip) && ip.matches(REGX_IP);
    }

    /**
     * 是否为 ip，或 * 为间隔的通配符地址
     */
    public static boolean isIpWildCard(String ip) {

        return StringUtils.isNotBlank(ip) && ip.matches(REGX_IP_WILDCARD);
    }

    /**
     * 检测参数是否在 ip 通配符里
     */
    public static boolean ipIsInWildCardNoCheck(String ipWildCard, String ip) {

        String[] s1 = ipWildCard.split("\\.");
        String[] s2 = ip.split("\\.");
        boolean isMatchedSeg = true;
        for (int i = 0; i < s1.length && !s1[i].equals("*"); i++) {

            if (!s1[i].equals(s2[i])) {

                isMatchedSeg = false;
                break;
            }
        }

        return isMatchedSeg;
    }

    /**
     * 是否为特定格式如：“10.10.10.1-10.10.10.99” 的 ip 段字符串
     */
    public static boolean isIPSegment(String ipSeg) {

        return StringUtils.isNotBlank(ipSeg) && ipSeg.matches(REGX_IP_SEG);
    }

    /**
     * 判断 ip 是否在指定网段中
     */
    public static boolean ipIsInNetNoCheck(String iparea, String ip) {

        int idx = iparea.indexOf('-');
        String[] sips = iparea.substring(0, idx).split("\\.");
        String[] sipe = iparea.substring(idx + 1).split("\\.");
        String[] sipt = ip.split("\\.");
        long ips = 0L, ipe = 0L, ipt = 0L;
        for (int i = 0; i < 4; ++i) {

            ips = ips << 8 | Integer.parseInt(sips[i]);
            ipe = ipe << 8 | Integer.parseInt(sipe[i]);
            ipt = ipt << 8 | Integer.parseInt(sipt[i]);
        }
        if (ips > ipe) {

            long t = ips;
            ips = ipe;
            ipe = t;
        }

        return ips <= ipt && ipt <= ipe;
    }

    /**
     * 校验 ip 是否符合过滤串规则
     *
     * @param filter 过滤 ip 列表，支持后缀 '*' 通配，支持网段如：`10.10.10.1-10.10.10.99`
     * @param ip     校验 ip 地址
     * @return boolean 结果
     */
    public static boolean isMatchedIp(String filter, String ip) {

        if (StringUtils.isEmpty(filter) || StringUtils.isEmpty(ip)) {

            return false;
        }
        String[] ips = filter.split(";");
        for (String iStr : ips) {

            if (isIP(iStr) && iStr.equals(ip)) {

                return true;
            } else if (isIpWildCard(iStr) && ipIsInWildCardNoCheck(iStr, ip)) {

                return true;
            } else if (isIPSegment(iStr) && ipIsInNetNoCheck(iStr, ip)) {

                return true;
            }
        }

        return false;
    }
}