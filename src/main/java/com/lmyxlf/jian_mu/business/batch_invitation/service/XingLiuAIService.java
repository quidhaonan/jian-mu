package com.lmyxlf.jian_mu.business.batch_invitation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lmyxlf.jian_mu.business.batch_invitation.model.entity.XingLiuAI;
import com.lmyxlf.jian_mu.business.batch_invitation.model.req.ReqXingLiuAI;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2025/6/22 19:08
 * @description 星流 AI
 * @since 17
 */
public interface XingLiuAIService extends IService<XingLiuAI> {

    Boolean batchInvite(ReqXingLiuAI reqXingLiuAI);
}