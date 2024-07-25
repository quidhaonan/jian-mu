package com.lmyxlf.jian_mu.global.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description
 * @since 17
 */
@Slf4j
public class HmacShaUtils {

    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String HMAC_MD5 = "HmacMD5";


    /**
     * 获取hmac
     *
     * @param content   加密内容
     * @param secret    加密密钥
     * @param algorithm 签名算法
     * @return
     */
    public static byte[] getHmac(byte[] content, byte[] secret, String algorithm) {
        byte[] result = null;
        try {
            Mac mac = Mac.getInstance(algorithm);
            SecretKey secretKey = new SecretKeySpec(secret, algorithm);
            mac.init(secretKey);
            result = mac.doFinal(content);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            // Monitor.error("get_hmac_error").tag("algorithm", algorithm).log("获取hmac出错，contentLength: {}, algorithm: {}, {}", content.length, algorithm, ExceptionUtil.stacktraceToString(e)).inc();
            log.error("获取hmac出错，contentLength: {}, algorithm: {}, {}", content.length, algorithm, ExceptionUtil.stacktraceToString(e));
        }
        return result;
    }

    /**
     * 获取 HmacSha256
     *
     * @param content 加密内容
     * @param secret  加密密钥
     * @return
     */
    public static byte[] getHmacSha256(byte[] content, byte[] secret) {
        return getHmac(content, secret, HMAC_SHA256);
    }

    /**
     * 获取 HmacSha256
     *
     * @param content 加密内容
     * @param secret  加密密钥
     * @return
     */
    public static byte[] getHmacSha256(String content, String secret) {
        return getHmacSha256(content.getBytes(StandardCharsets.UTF_8), secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 获取 HmacSha256
     *
     * @param content 加密内容
     * @param secret  加密密钥
     * @return
     */
    public static String getHmacSha256Str(String content, String secret) {
        return bytesToHexString(getHmacSha256(content, secret));
    }

    /**
     * 获取 HmacSha1
     *
     * @param content 加密内容
     * @param secret  加密密钥
     * @return
     */
    public static byte[] getHmacSha1(byte[] content, byte[] secret) {
        return getHmac(content, secret, HMAC_SHA1);
    }

    /**
     * 获取 HmacSha1
     *
     * @param content 加密内容
     * @param secret  加密密钥
     * @return
     */
    public static byte[] getHmacSha1(String content, String secret) {
        return getHmacSha1(content.getBytes(StandardCharsets.UTF_8), secret.getBytes(StandardCharsets.UTF_8));
    }


    /**
     * 获取 HmacMD5
     *
     * @param content 加密内容
     * @param secret  加密密钥
     * @return
     */
    public static byte[] getHmacMD5(byte[] content, byte[] secret) {
        return getHmac(content, secret, HMAC_MD5);
    }

    /**
     * 获取 HmacMD5
     *
     * @param content 加密内容
     * @param secret  加密密钥
     * @return
     */
    public static byte[] getHmacMD5(String content, String secret) {
        return getHmacMD5(content.getBytes(StandardCharsets.UTF_8), secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 获取 HmacMD5
     *
     * @param content 加密内容
     * @param secret  加密密钥
     * @return
     */
    public static String getHmacMD5Str(String content, String secret) {
        return bytesToHexString(getHmacMD5(content, secret));
    }

    protected static String bytesToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (byte b : bArray) {
            sTemp = Integer.toHexString(0xFF & b);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toLowerCase());
        }
        return sb.toString();
    }
}