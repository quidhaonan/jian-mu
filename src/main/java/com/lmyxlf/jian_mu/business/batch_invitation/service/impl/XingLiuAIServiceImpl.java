package com.lmyxlf.jian_mu.business.batch_invitation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.business.batch_invitation.common.BIAsyncMethods;
import com.lmyxlf.jian_mu.business.batch_invitation.dao.XingLiuAIDao;
import com.lmyxlf.jian_mu.business.batch_invitation.model.entity.XingLiuAI;
import com.lmyxlf.jian_mu.business.batch_invitation.model.req.ReqXingLiuAI;
import com.lmyxlf.jian_mu.business.batch_invitation.service.XingLiuAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2025/6/22 19:08
 * @description 星流 AI
 * @since 17
 */
@Slf4j
@Service("xingLiuAIService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class XingLiuAIServiceImpl extends ServiceImpl<XingLiuAIDao, XingLiuAI> implements XingLiuAIService {

    private final BIAsyncMethods biAsyncMethods;

    @Override
    public Boolean batchInvite(ReqXingLiuAI reqXingLiuAI) {

        log.info("星流 AI 批量邀请，reqXingLiuAI：{}", reqXingLiuAI);
        String inviteCode = reqXingLiuAI.getInviteCode();
        Integer num = reqXingLiuAI.getNum();

        // 生成 0 到 num-1 的流
        IntStream.range(0, num)
                .forEach(i -> biAsyncMethods.xingLiuInviteBatch(inviteCode));

        return Boolean.TRUE;
    }
}