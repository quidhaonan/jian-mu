package com.lmyxlf.jian_mu.business.raising_numbers.util;

import com.lmyxlf.jian_mu.business.raising_numbers.constant.RNConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/26 3:31
 * @description 随机休眠
 * @since 17
 */
@Slf4j
public class RandomHibernationUtil {

    public static Boolean randomHibernation() {

        return randomHibernation(RNConstant.SLEEP_DURATION);
    }

    public static Boolean randomHibernation(Long millis) {

        long nextLong = ThreadLocalRandom.current().nextLong(millis);
        try {
            Thread.sleep(nextLong);
        } catch (InterruptedException e) {
            log.error("休眠失败，nextInt：{}", nextLong);
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}