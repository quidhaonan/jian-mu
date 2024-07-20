package com.lmyxlf.jian_mu.global.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/20 1:30
 * @description 随机工具类
 * @since 17
 */
public class RandomUtil {

    private final static char[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public static String generateRandomStr(int len) {
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            int j = ThreadLocalRandom.current().nextInt(CHARS.length);
            chars[i] = CHARS[j];
        }
        return new String(chars);
    }
}