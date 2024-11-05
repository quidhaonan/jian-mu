package com.lmyxlf.jian_mu.admin.controller;

import com.lmyxlf.jian_mu.admin.annotation.Log;
import com.lmyxlf.jian_mu.admin.model.enums.BusinessTypeEnum;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysLoginInfo;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysLoginInfo;
import com.lmyxlf.jian_mu.admin.service.SysLoginInfoService;
import com.lmyxlf.jian_mu.admin.service.SysPasswordService;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import com.lmyxlf.jian_mu.global.model.PageData;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/7 1:57
 * @description 系统访问记录
 * @since 17
 */
@RestController
@Api(tags = "系统访问记录")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.admin.urlPrefix}/monitor/logininfo")
public class SysLoginInfoController {

    private final SysLoginInfoService sysLoginInfoService;
    private final SysPasswordService sysPasswordService;

    @PreAuthorize("@permissionService.hasPermi('monitor:logininfor:list')")
    @PostMapping("/list")
    public LmyXlfResult<PageData<RespSysLoginInfo>> list(@RequestBody ReqSysLoginInfo reqSysLoginInfo) {

        return LmyXlfResult.ok(new PageData<>(sysLoginInfoService.list(reqSysLoginInfo)));
    }

    @Log(title = "登录日志", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@permissionService.hasPermi('monitor:logininfor:export')")
    @PostMapping("/export")
    public void export(@RequestBody ReqSysLoginInfo reqSysLoginInfo, HttpServletResponse response) {

        sysLoginInfoService.export(reqSysLoginInfo, response);
    }

    @PreAuthorize("@permissionService.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登录日志", businessType = BusinessTypeEnum.DELETE)
    @PostMapping("/remove")
    public LmyXlfResult<Boolean> remove(@RequestBody ReqSysLoginInfo reqSysLoginInfo) {

        return LmyXlfResult.ok(sysLoginInfoService.remove(reqSysLoginInfo));
    }

    @PreAuthorize("@permissionService.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登录日志", businessType = BusinessTypeEnum.CLEAN)
    @DeleteMapping("/clean")
    public LmyXlfResult<Boolean> clean() {

        return LmyXlfResult.ok(sysLoginInfoService.clean());
    }

    @PreAuthorize("@permissionService.hasPermi('monitor:logininfor:unlock')")
    @Log(title = "账户解锁", businessType = BusinessTypeEnum.OTHER)
    @GetMapping("/unlock/{userName}")
    public LmyXlfResult<Object> unlock(@PathVariable("userName") String userName) {

        sysPasswordService.clearLoginRecordCache(userName);
        return LmyXlfResult.ok();
    }
}