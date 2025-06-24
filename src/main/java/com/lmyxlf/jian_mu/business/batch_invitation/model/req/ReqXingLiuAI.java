package com.lmyxlf.jian_mu.business.batch_invitation.model.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2025/6/22 19:22
 * @description 星流 AI
 * @since 17
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "星流 AI 请求体")
public class ReqXingLiuAI {

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 邀请数量
     */
    private Integer num;
}