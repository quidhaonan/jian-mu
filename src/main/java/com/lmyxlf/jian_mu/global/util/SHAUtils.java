package com.lmyxlf.jian_mu.global.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description 计算 SHA
 * @since 17
 */
public class SHAUtils {

    private static final String SHA1 = "SHA1";

    /**
     * 获取 sha1 签名
     * @param str
     * @return
     */
    public static String sha1(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        return sha1(bytes);
    }

    /**
     * 获取 sha1 签名
     * @param bytes
     * @return
     */
    public static String sha1(byte[] bytes) {
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest mdTemp = MessageDigest.getInstance(SHA1);
            mdTemp.update(bytes);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }
}