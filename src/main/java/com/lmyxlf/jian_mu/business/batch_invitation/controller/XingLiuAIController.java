package com.lmyxlf.jian_mu.business.batch_invitation.controller;

import com.lmyxlf.jian_mu.business.batch_invitation.model.req.ReqXingLiuAI;
import com.lmyxlf.jian_mu.business.batch_invitation.service.XingLiuAIService;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2025/6/22 19:04
 * @description 星流 AI
 * @since 17
 */
@RestController
@Api(tags = "星流 AI")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.batch_invitation.urlPrefix}/xingliu")
public class XingLiuAIController {

    private final XingLiuAIService xingLiuAIService;

    @PostMapping("/batch_invite")
    @ApiOperation(value = "批量邀请用户")
    public LmyXlfResult<Boolean> invite(@RequestBody ReqXingLiuAI reqXingLiuAI) {

        return LmyXlfResult.ok(xingLiuAIService.batchInvite(reqXingLiuAI));
    }
}