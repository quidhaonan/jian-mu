package com.lmyxlf.jian_mu.admin.controller;

import com.lmyxlf.jian_mu.admin.annotation.Log;
import com.lmyxlf.jian_mu.admin.model.enums.BusinessTypeEnum;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysOperLog;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysOperLog;
import com.lmyxlf.jian_mu.admin.service.SysOperLogService;
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
 * @date 2024/9/13 23:40
 * @description
 * @since 17
 */
@RestController
@Api(tags = "操作日志记录")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.admin.urlPrefix}/monitor/operlog")
public class SysOperlogController {

    private final SysOperLogService sysOperLogService;

    @PreAuthorize("@permissionService.hasPermi('monitor:operlog:list')")
    @PostMapping("/list")
    public LmyXlfResult<PageData<RespSysOperLog>> list(@RequestBody ReqSysOperLog reqSysOperLog) {

        return LmyXlfResult.ok(new PageData<>(sysOperLogService.list(reqSysOperLog)));
    }

    @Log(title = "操作日志", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@permissionService.hasPermi('monitor:operlog:export')")
    @PostMapping("/export")
    public void export(@RequestBody ReqSysOperLog reqSysOperLog, HttpServletResponse response) {

        sysOperLogService.export(reqSysOperLog, response);
    }

    @Log(title = "操作日志", businessType = BusinessTypeEnum.DELETE)
    @PreAuthorize("@permissionService.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/remove")
    public LmyXlfResult<Boolean> remove(@RequestBody ReqSysOperLog reqSysOperLog) {

        return LmyXlfResult.ok(sysOperLogService.remove(reqSysOperLog));
    }

    @Log(title = "操作日志", businessType = BusinessTypeEnum.CLEAN)
    @PreAuthorize("@permissionService.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/clean")
    public LmyXlfResult<Boolean> clean() {

        return LmyXlfResult.ok(sysOperLogService.clean());
    }
}