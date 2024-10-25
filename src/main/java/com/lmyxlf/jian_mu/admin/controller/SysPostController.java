package com.lmyxlf.jian_mu.admin.controller;

import com.lmyxlf.jian_mu.admin.annotation.Log;
import com.lmyxlf.jian_mu.admin.model.enums.BusinessTypeEnum;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysPost;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysPost;
import com.lmyxlf.jian_mu.admin.service.SysPostService;
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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/30 18:53
 * @description
 * @since 17
 */
@RestController
@Api(tags = "岗位信息操作处理")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.admin.urlPrefix}/system/post")
public class SysPostController {

    private final SysPostService sysPostService;

    /**
     * 获取岗位列表
     */
    @PreAuthorize("@ss.hasPermi('system:post:list')")
    @PostMapping("/list")
    public LmyXlfResult<PageData<RespSysPost>> list(@RequestBody ReqSysPost reqSysPost) {

        return LmyXlfResult.ok(new PageData<>(sysPostService.list(reqSysPost)));
    }

    @Log(title = "岗位管理", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:post:export')")
    @PostMapping("/export")
    public void export(@RequestBody ReqSysPost reqSysPost, HttpServletResponse response) {

        sysPostService.export(reqSysPost, response);
    }

    /**
     * 根据岗位编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:post:query')")
    @GetMapping(value = "/{id}")
    public LmyXlfResult<RespSysPost> getInfo(@PathVariable Integer id) {

        return LmyXlfResult.ok(sysPostService.getInfo(id));
    }

    /**
     * 新增岗位
     */
    @PreAuthorize("@ss.hasPermi('system:post:add')")
    @Log(title = "岗位管理", businessType = BusinessTypeEnum.INSERT)
    @PostMapping("/add")
    public LmyXlfResult<Boolean> add(@Validated({Insert.class}) @RequestBody ReqSysPost reqSysPost) {

        return LmyXlfResult.ok(sysPostService.add(reqSysPost));
    }

    /**
     * 修改岗位
     */
    @PreAuthorize("@ss.hasPermi('system:post:edit')")
    @Log(title = "岗位管理", businessType = BusinessTypeEnum.UPDATE)
    @PostMapping("/edit")
    public LmyXlfResult<Boolean> edit(@Validated({Update.class}) @RequestBody ReqSysPost reqSysPost) {

        return LmyXlfResult.ok(sysPostService.edit(reqSysPost));
    }

    /**
     * 删除岗位
     */
    @PreAuthorize("@ss.hasPermi('system:post:remove')")
    @Log(title = "岗位管理", businessType = BusinessTypeEnum.DELETE)
    @PostMapping("/remove")
    public LmyXlfResult<Boolean> remove(@Validated({Delete.class}) @RequestBody ReqSysPost reqSysPost) {

        return LmyXlfResult.ok(sysPostService.remove(reqSysPost));
    }

    /**
     * 获取岗位选择框列表
     */
    @GetMapping("/optionselect")
    public LmyXlfResult<List<RespSysPost>> optionselect() {

        return LmyXlfResult.ok(sysPostService.optionselect());
    }
}