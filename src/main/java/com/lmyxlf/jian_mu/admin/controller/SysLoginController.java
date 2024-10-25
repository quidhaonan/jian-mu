package com.lmyxlf.jian_mu.admin.controller;

import com.lmyxlf.jian_mu.admin.model.req.ReqLoginBody;
import com.lmyxlf.jian_mu.admin.model.resp.RespLoginUserInfo;
import com.lmyxlf.jian_mu.admin.model.resp.RespRouter;
import com.lmyxlf.jian_mu.admin.service.SysLoginService;
import com.lmyxlf.jian_mu.global.constant.LmyXlfReqParamConstant;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/21 17:32
 * @description
 * @since 17
 */
@RestController
@Api(tags = "登录验证")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.admin.urlPrefix}/auth")
public class SysLoginController {

    private final SysLoginService sysLoginService;

    /**
     * 登录方法
     *
     * @param reqLoginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public LmyXlfResult<Map<String, String>> login(@RequestBody ReqLoginBody reqLoginBody) {

        // 生成令牌
        String token = sysLoginService.login(reqLoginBody);

        return LmyXlfResult.ok(Map.ofEntries(
                Map.entry(LmyXlfReqParamConstant.TOKEN, token)
        ));
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public LmyXlfResult<RespLoginUserInfo> getInfo() {

        return LmyXlfResult.ok(sysLoginService.getInfo());
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public LmyXlfResult<List<RespRouter>> getRouters() {

        return LmyXlfResult.ok(sysLoginService.getRouters());
    }
}