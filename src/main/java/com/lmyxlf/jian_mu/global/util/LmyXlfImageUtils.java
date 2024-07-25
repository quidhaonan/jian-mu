package com.lmyxlf.jian_mu.global.util;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.img.ImgUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description springMcv 自定义的一些配置
 * @since 17
 */
@Slf4j
public class LmyXlfImageUtils {

    public static String compressBase64Str(String base64Str, float scale) {
        byte[] decode = Base64Decoder.decode(base64Str);
        return compressBase64Str(decode, scale);
    }

    public static String compressBase64Str(byte[] decode, float scale) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decode);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImgUtil.scale(byteArrayInputStream, byteArrayOutputStream, scale);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String compressBase64 = Base64Encoder.encode(bytes);
        log.info("bytes:{}", compressBase64.getBytes().length);
        return compressBase64;
    }
}