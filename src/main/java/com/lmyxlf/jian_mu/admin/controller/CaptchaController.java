package com.lmyxlf.jian_mu.admin.controller;

import com.lmyxlf.jian_mu.admin.service.CaptchaService;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/28 19:57
 * @description
 * @since 17
 */
@RestController
@Api(tags = "验证码操作处理")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.admin.urlPrefix}/captcha")
public class CaptchaController {

    private final CaptchaService captchaService;

    @GetMapping("/captchaImage")
    @ApiOperation(value = "生成验证码")
    public LmyXlfResult<Map<String, Object>> getCode() {

        return LmyXlfResult.ok(captchaService.getCode());
    }
}