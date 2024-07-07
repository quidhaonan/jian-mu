package com.lmyxlf.jian_mu.global.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description SM4 加密 128 位 EBC 模式工具类
 * @since 17
 */
@Slf4j
public class SM4Util {

    private static final String SM4_KEY = "af218764d8079d21" ;

    public static String encrypt(String data) {
        if (StrUtil.isBlank(data)) {
            return data;
        }

        try {
            SM4 sm4 = SmUtil.sm4(SM4_KEY.getBytes(StandardCharsets.UTF_8));
            return sm4.encryptHex(data, StandardCharsets.UTF_8);
        } catch (Exception e) {
            // Monitor.error("sm4_encrypt_error").log("SM4加密异常：content: {}, {}", data, ExceptionUtil.stacktraceToString(e)).inc();
            log.error("SM4 加密异常：content: {}, {}", data, ExceptionUtil.stacktraceToString(e));
        }
        return null;
    }


    public static String decrypt(String data) {
        if (StrUtil.isBlank(data)) {
            return data;
        }

        try {
            SM4 sm4 = SmUtil.sm4(SM4_KEY.getBytes(StandardCharsets.UTF_8));
            return sm4.decryptStr(data, StandardCharsets.UTF_8);
        } catch (Exception e) {
            // Monitor.error("sm4_decrypt_error").log("SM4解密异常：content: {}, {}", data, ExceptionUtil.stacktraceToString(e)).inc();
            log.error("SM4 解密异常：content: {}, {}", data, ExceptionUtil.stacktraceToString(e));
        }
        return null;
    }


}