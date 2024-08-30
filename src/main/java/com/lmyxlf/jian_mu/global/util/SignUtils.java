package com.lmyxlf.jian_mu.global.util;

import com.lmyxlf.jian_mu.global.constant.LmyXlfReqParamConstant;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description 生成签名工具类
 * @since 17
 */
public class SignUtils {

    /**
     * 校验签名工具
     *
     * @param map    参数
     * @param secret 私钥
     * @return 校验是否成功
     */
    public static boolean verifySign(Map<String, String[]> map, String secret) {
        final String[] signs = map.remove(LmyXlfReqParamConstant.KEY_SIGN);
        final String sign = generateSign(map, secret);
        return sign.equals(signs[0]);
    }

    /**
     * 同行者校验签名工具
     *
     * @param map    参数
     * @param secret 私钥
     * @return 校验是否成功
     */
    public static boolean verifySign2(Map<String, String> map, String secret) {
        final String signs = map.remove(LmyXlfReqParamConstant.KEY_SIGN);
        final String sign = generateSign2(map, secret);
        return sign.equals(signs);
    }

    /**
     * @param params 签名参数
     * @param secret 签名私钥
     * @return sign
     */
    public static String generateSign(Map<String, String[]> params, String secret) {
        // 1、获取排序字符串
        String sortStr = generateSortStr2(params);
        // 2、把排序字符串进行 BASE64 编码
        byte[] base64EncodedStr = Base64.getEncoder().encode(sortStr.getBytes(StandardCharsets.UTF_8));
        // 3、使⽤ secret 对 base64EncodedStr 进⾏ HMAC-SHA256 哈希得到字节数组
        byte[] hmacSha256 = HmacShaUtils.getHmacSha256(base64EncodedStr, secret.getBytes(StandardCharsets.UTF_8));
        // 4、对字节数组进行 MD5 计算
        return MD5Utils.encrypt(hmacSha256);
    }

    /**
     * @param params 签名参数
     * @param secret 签名私钥
     * @return sign
     */
    public static String generateSign2(Map<String, String> params, String secret) {
        // 1、获取排序字符串
        String sortStr = generateSortStr(params);
        // 2、把排序字符串进行 BASE64 编码
        byte[] base64EncodedStr = Base64.getEncoder().encode(sortStr.getBytes(StandardCharsets.UTF_8));
        // 3、使⽤ secret 对 base64EncodedStr 进⾏ HMAC-SHA256 哈希得到字节数组
        byte[] hmacSha256 = HmacShaUtils.getHmacSha256(base64EncodedStr, secret.getBytes(StandardCharsets.UTF_8));
        // 4、对字节数组进行 MD5 计算
        return MD5Utils.encrypt(hmacSha256);
    }

    /**
     * 按字典排序生成连接字符串
     *
     * @param params 请求的参数
     * @return 按字典排序后生成的字符串
     */
    public static String generateSortStr(Map<String, String> params) {
        if (params.isEmpty()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        // 按字典排序
        Object[] keys = params.keySet().toArray();
        Arrays.sort(keys);

        stringBuilder.append(keys[0]).append("=").append(params.get((String) keys[0]));

        for (int i = 1; i < keys.length; i++) {
            Object key = keys[i];
            stringBuilder.append("&").append(key).append("=").append(params.get((String) key));
        }
        return stringBuilder.toString();
    }

    /**
     * 按字典排序生成连接字符串
     *
     * @param params 请求的参数
     * @return 按字典排序后生成的字符串
     */
    public static String generateSortStr2(Map<String, String[]> params) {
        if (params.isEmpty()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        // 按字典排序
        Object[] keys = params.keySet().toArray();
        Arrays.sort(keys);

        // 默认每一个参数都只有一个值
        final String[] obj = params.get((String) keys[0]);
        stringBuilder.append(keys[0]).append("=").append(obj[0]);

        for (int i = 1; i < keys.length; i++) {
            Object key = keys[i];
            final String[] values = params.get((String) key);
            if (values.length > 0) {
                stringBuilder.append("&").append(key).append("=").append(values[0]);
            } else {
                stringBuilder.append("&").append(key).append("=");
            }
        }
        return stringBuilder.toString();
    }

//    public static void main(String[] args) {
//        Map<String, String[]> map = new HashMap<>(16);
//        map.put("app_id", new String[]{"lmyXlfAppid"});
//        map.put("nonce", new String[]{"a1d9s8cxjd7sdf2"});
//        map.put("phone", new String[]{"18297962986"});
//        map.put("order_id", new String[]{"22222249"});
//        map.put("reason", new String[]{"付错油站了"});
//        map.put("aid", new String[]{"lmyXlfAccountId"});
//        long l = System.currentTimeMillis();
//        System.out.println(l);
//        map.put("_rt", new String[]{"1518279950096"});
//        System.out.println(generateSortStr2(map));
//        String lmyXlf = generateSign(map, "lmyXlf");
//        System.out.println(lmyXlf);
//    }
}