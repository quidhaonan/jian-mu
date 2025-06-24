package com.lmyxlf.jian_mu.business.batch_invitation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2025/6/22 17:15
 * @description 椰子接码平台拉黑号码返回
 * @since 17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class YeZiAddBlacklist {

    /**
     * 结果提示
     */
    private String message;

    /**
     * 数据
     */
    private List<Object> data;
}