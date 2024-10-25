package com.lmyxlf.jian_mu.admin.controller;

import com.lmyxlf.jian_mu.admin.annotation.Log;
import com.lmyxlf.jian_mu.admin.model.enums.BusinessTypeEnum;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysDept;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysDept;
import com.lmyxlf.jian_mu.admin.service.SysDeptService;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/17 2:04
 * @description
 * @since 17
 */
@RestController
@Api(tags = "部门信息")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.admin.urlPrefix}/system/dept")
public class SysDeptController {

    private SysDeptService sysDeptService;

    /**
     * 获取部门列表
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @PostMapping("/list")
    public LmyXlfResult<List<RespSysDept>> list(@RequestBody ReqSysDept reqSysDept) {

        return LmyXlfResult.ok(sysDeptService.list(reqSysDept));
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{id}")
    public LmyXlfResult<List<RespSysDept>> excludeChild(@PathVariable(value = "id", required = false) Integer id) {

        return LmyXlfResult.ok(sysDeptService.excludeChild(id));
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{id}")
    public LmyXlfResult<RespSysDept> getInfo(@PathVariable("id") Integer id) {

        return LmyXlfResult.ok(sysDeptService.getInfo(id));
    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessTypeEnum.INSERT)
    @PostMapping("/add")
    public LmyXlfResult<Boolean> add(@Validated @RequestBody ReqSysDept reqSysDept) {

        return LmyXlfResult.ok(sysDeptService.add(reqSysDept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessTypeEnum.UPDATE)
    @PostMapping("/edit")
    public LmyXlfResult<Boolean> edit(@Validated @RequestBody ReqSysDept reqSysDept) {

        return LmyXlfResult.ok(sysDeptService.edit(reqSysDept));
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/{id}")
    public LmyXlfResult<Boolean> remove(@PathVariable("id") Integer id) {

        return LmyXlfResult.ok(sysDeptService.remove(id));
    }
}