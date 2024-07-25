package com.lmyxlf.jian_mu.business.raising_numbers.model.resp;

import com.lmyxlf.jian_mu.business.raising_numbers.model.entity.RaisingNumbers;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

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

    private Data data;

    private String msg;

    /**
     * 错误 token
     */
    private RaisingNumbers raisingNumbers;

    @lombok.Data
    @Accessors(chain = true)
    public static class Data{

        private Integer days;

        private Integer mdays;

        private Integer reward;

        private LocalDateTime qtime;

        private String url;

        private Integer credit;
    }
}