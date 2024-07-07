package com.lmyxlf.jian_mu.global.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description AES 加密 128 位 CBC 模式工具类
 * @since 17
 */
@Slf4j
public class AESUtil {
    private static final int MIN_KEY_LENGTH = 16;
    private static final int NUL_CHARACTERS_ASCII = 0;

    /**
     * 加密
     * AES加密128位CBC模式
     *
     * @param content
     * @param key
     * @param vi
     * @return
     */
    public static String encrypt(String content, String key, String vi) {
        try {
            byte[] raw = key.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            // "算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec ips = new IvParameterSpec(vi.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ips);
            byte[] encrypted = cipher.doFinal(content.getBytes());

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            // Monitor.error("aes_encrypt_error").log("AES加密异常：content: {}, {}", content, ExceptionUtil.stacktraceToString(e)).inc();
            log.error("AES 加密异常：content: {}, {}", content, ExceptionUtil.stacktraceToString(e));
        }
        return null;
    }

    /**
     * 解密
     * AES加密128位CBC模式
     *
     * @param content
     * @param key
     * @param vi
     * @return
     */
    public static String decrypt(String content, String key, String vi) {
        try {
            byte[] raw = key.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(vi.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ips);
            byte[] encrypted1 = Base64.getDecoder().decode(content);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original);
        } catch (Exception ex) {
            // Monitor.error("aes_decrypt_error").log("AES解密异常：content: {}, {}", content, ExceptionUtil.stacktraceToString(ex)).inc();
            log.error("AES 解密异常：content: {}, {}", content, ExceptionUtil.stacktraceToString(ex));
            return null;
        }
    }
}