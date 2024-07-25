package com.lmyxlf.jian_mu.global.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description 计算 MD5
 * @since 17
 */
@Slf4j
public class MD5Utils {

    private static final String MD5 = "MD5" ;

    /**
     * 计算 MD5 加密值，结果为小写字符串
     *
     * @param content 加密内容
     * @return String
     */
    public static String encrypt(byte[] content) {
        try {
            MessageDigest md5 = MessageDigest.getInstance(MD5);
            md5.update(content);

            byte[] bytes = md5.digest();

            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                result.append(Integer.toHexString((0x000000FF & b) | 0xFFFFFF00).substring(6));
            }
            return result.toString();
        } catch (Exception e) {
            // Monitor.error("md5_encrypt_error").log("md5加密错误：{}", ExceptionUtil.getMessage(e)).inc();
            log.error("md5 加密错误：{}", ExceptionUtil.getMessage(e));
        }
        return "" ;
    }

    /**
     * 计算 MD5 加密值，结果为小写字符串
     *
     * @param content 加密内容
     * @return String
     */
    public static String encrypt(String content) {
        return encrypt(content.getBytes(StandardCharsets.UTF_8));
    }
}