package com.lmyxlf.jian_mu.admin.controller;

import com.lmyxlf.jian_mu.admin.annotation.Log;
import com.lmyxlf.jian_mu.admin.model.enums.BusinessTypeEnum;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysNotice;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysNotice;
import com.lmyxlf.jian_mu.admin.service.SysNoticeService;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import com.lmyxlf.jian_mu.global.model.PageData;
import com.lmyxlf.jian_mu.global.validation.group.Delete;
import com.lmyxlf.jian_mu.global.validation.group.Insert;
import com.lmyxlf.jian_mu.global.validation.group.Update;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/30 12:39
 * @description
 * @since 17
 */
@RestController
@Api(tags = "公告，信息操作处理")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.admin.urlPrefix}/system/notice")
public class SysNoticeController {

    private final SysNoticeService sysNoticeService;

    /**
     * 获取通知公告列表
     */
    @PreAuthorize("@permissionService.hasPermi('system:notice:list')")
    @PostMapping("/list")
    public LmyXlfResult<PageData<RespSysNotice>> list(@RequestBody ReqSysNotice reqSysNotice) {

        return LmyXlfResult.ok(new PageData<>(sysNoticeService.list(reqSysNotice)));
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @PreAuthorize("@permissionService.hasPermi('system:notice:query')")
    @GetMapping(value = "/{id}")
    public LmyXlfResult<RespSysNotice> getInfo(@PathVariable Integer id) {

        return LmyXlfResult.ok(sysNoticeService.getInfo(id));
    }

    /**
     * 新增通知公告
     */
    @PreAuthorize("@permissionService.hasPermi('system:notice:add')")
    @Log(title = "通知公告", businessType = BusinessTypeEnum.INSERT)
    @PostMapping("/add")
    public LmyXlfResult<Boolean> add(@Validated({Insert.class}) @RequestBody ReqSysNotice reqSysNotice) {

        return LmyXlfResult.ok(sysNoticeService.add(reqSysNotice));
    }

    /**
     * 修改通知公告
     */
    @PreAuthorize("@permissionService.hasPermi('system:notice:edit')")
    @Log(title = "通知公告", businessType = BusinessTypeEnum.UPDATE)
    @PostMapping("/edit")
    public LmyXlfResult<Boolean> edit(@Validated({Update.class}) @RequestBody ReqSysNotice reqSysNotice) {

        return LmyXlfResult.ok(sysNoticeService.edit(reqSysNotice));
    }

    /**
     * 删除通知公告
     */
    @PreAuthorize("@permissionService.hasPermi('system:notice:remove')")
    @Log(title = "通知公告", businessType = BusinessTypeEnum.DELETE)
    @PostMapping("/remove")
    public LmyXlfResult<Boolean> remove(@Validated({Delete.class}) @RequestBody ReqSysNotice reqSysNotice) {

        return LmyXlfResult.ok(sysNoticeService.remove(reqSysNotice));
    }
}