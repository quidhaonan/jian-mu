package com.lmyxlf.jian_mu.business.batch_invitation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2025/6/22 16:35
 * @description 椰子接码平台获取短信返回
 * @since 17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class YeZiGetMessageDto {

    /**
     * 结果提示
     */
    private String message;

    /**
     * 验证码
     */
    private String code;

    // /**
    //  * 防止网络波动读不到短信多次提示次数
    //  */
    // private Integer 防止网络波动读不到短信多次提示次数;

    /**
     * 数据
     */
    private List<Data> data;

    static class Data {

        /**
         * 项目 id
         */
        private String projectId;

        /**
         * 短信完整内容
         */
        private String modle;

        /**
         * 手机号
         */
        private String phone;

        /**
         * 项目类型
         */
        private Integer projectType;
    }
}