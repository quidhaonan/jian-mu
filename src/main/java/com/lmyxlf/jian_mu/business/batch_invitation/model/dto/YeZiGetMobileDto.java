package com.lmyxlf.jian_mu.business.batch_invitation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2025/6/22 16:09
 * @description 椰子接码平台取卡返回
 * @since 17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class YeZiGetMobileDto {

    /**
     * 结果提示
     */
    private String message;

    /**
     * 手机号
     */
    private String mobile;

    // /**
    //  * 剩余取卡数如果小于 10 时 停止请求,否则拉黑 ip 需要 1 个小时才释放
    //  */
    // private Integer 1分钟内剩余取卡数;

    // /**
    //  * 上线时间
    //  */
    // private LocalDateTime 上线时间;

    /**
     * 号码归属地
     */
    private String operator;

    // /**
    //  * 上次短信时间，时间戳
    //  */
    // private String 上次短信时间;

    // /**
    //  * 上次短信时间 2
    //  */
    // private String 上次短信时间 2;

    /**
     * 数据
     */
    private List<Object> data;
}