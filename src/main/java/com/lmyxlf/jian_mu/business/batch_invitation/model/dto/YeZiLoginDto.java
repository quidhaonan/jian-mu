package com.lmyxlf.jian_mu.business.batch_invitation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2025/6/22 15:12
 * @description 椰子接码平台登录返回
 * @since 17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class YeZiLoginDto {

    /**
     * token
     */
    private String token;

    /**
     * 登录消息
     */
    private String message;

    /**
     * 数据
     */
    private List<Data> data;

    static class Data {

        /**
         * 用户 id
         */
        private String id;

        /**
         * 余额
         */
        private String money;

        /**
         * 余额
         */
        private String money_1;

        /**
         * 用户信息
         */
        private String leixing;

        /**
         * 登录 ip
         */
        private String ip;
    }
}