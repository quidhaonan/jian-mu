package com.lmyxlf.jian_mu.business.batch_invitation.model.enums;

import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/23 1:34
 * @description
 * @since 17
 */
@Slf4j
@Getter
@AllArgsConstructor
public enum FtpClientType {

    /**
     * 01 号 ftp
     */
    FTP_01(1, "http://test0101.3vhost.work");

    private final int type;
    private final String domain;

    /**
     * 根据 type 获得 FtpClientType 枚举
     *
     * @param type
     * @return
     */
    public static FtpClientType getFtpClientTypeByType(Integer type) {
        FtpClientType[] values = values();
        for (FtpClientType item : values) {
            if (item.getType() == type) {
                return item;
            }
        }

        log.error("无效 FtpClientType 类型，type：{}", type);
        throw new LmyXlfException("无效类型");
    }
}