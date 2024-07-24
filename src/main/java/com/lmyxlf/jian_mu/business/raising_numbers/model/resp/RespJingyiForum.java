package com.lmyxlf.jian_mu.business.raising_numbers.model.resp;

import com.lmyxlf.jian_mu.business.raising_numbers.model.entity.RaisingNumbers;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/24 22:45
 * @description 精易论坛请求返回体
 * @since 17
 */
@Data
@Accessors(chain = true)
public class RespJingyiForum {

    private String status;

    private String msg;

    /**
     * 错误 token
     */
    private RaisingNumbers raisingNumbers;
}