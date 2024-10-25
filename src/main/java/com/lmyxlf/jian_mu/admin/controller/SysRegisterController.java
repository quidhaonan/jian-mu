package com.lmyxlf.jian_mu.admin.controller;

import com.lmyxlf.jian_mu.admin.model.req.ReqLoginBody;
import com.lmyxlf.jian_mu.admin.service.SysRegisterService;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import com.lmyxlf.jian_mu.global.validation.group.Other;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/1 13:06
 * @description
 * @since 17
 */
@RestController
@Api(tags = "注册验证")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.admin.urlPrefix}/system/verify")
public class SysRegisterController {

    private final SysRegisterService sysRegisterService;

    @PostMapping("/register")
    public LmyXlfResult<Boolean> register(@Validated({Other.class}) @RequestBody ReqLoginBody reqLoginBody) {

        return LmyXlfResult.ok(sysRegisterService.register(reqLoginBody));
    }
}