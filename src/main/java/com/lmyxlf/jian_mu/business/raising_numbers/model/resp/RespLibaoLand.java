package com.lmyxlf.jian_mu.business.raising_numbers.model.resp;

import com.lmyxlf.jian_mu.business.raising_numbers.model.entity.RaisingNumbers;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/24 12:37
 * @description 丽宝乐园请求返回体
 * @since 17
 */
@Data
@Accessors(chain = true)
public class RespLibaoLand {

    private String m;

    private String e;

    private D d;

    /**
     * 错误 token
     */
    private RaisingNumbers raisingNumbers;

    @Data
    @Accessors(chain = true)
    public static class D{

        private String Photo;

        private String NickName;

        private String Mobile;

        private String Title;

        private String Notice;

        private String Msg;

        private String IsCheckIn;

        private String LotteryCount;

        private String CouponRuleID;

        private String PutInID;

        private String RewardType;

        private String IsGiveSucess;

        private String LotteryID;

        private String LotteryVersion;
    }
}