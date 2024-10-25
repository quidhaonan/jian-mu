package com.lmyxlf.jian_mu.admin.controller;

import com.lmyxlf.jian_mu.admin.annotation.Log;
import com.lmyxlf.jian_mu.admin.model.enums.BusinessTypeEnum;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysDictType;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysDictType;
import com.lmyxlf.jian_mu.admin.service.SysDictTypeService;
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
 * @date 2024/9/20 22:51
 * @description
 * @since 17
 */
@RestController
@Api(tags = "数据字典信息")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.admin.urlPrefix}/system/dict/type")
public class SysDictTypeController {

    private final SysDictTypeService sysDictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @PostMapping("/list")
    public LmyXlfResult<PageData<RespSysDictType>> list(@RequestBody ReqSysDictType reqSysDictType) {

        return LmyXlfResult.ok(sysDictTypeService.list(reqSysDictType));
    }

    @Log(title = "字典类型", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @PostMapping("/export")
    public void export(ReqSysDictType reqSysDictType, HttpServletResponse response) {

        sysDictTypeService.export(reqSysDictType, response);
    }

    /**
     * 查询字典类型详细
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{id}")
    public LmyXlfResult<RespSysDictType> getInfo(@PathVariable Integer id) {

        return LmyXlfResult.ok(sysDictTypeService.getInfo(id));
    }

    /**
     * 新增字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典类型", businessType = BusinessTypeEnum.INSERT)
    @PostMapping("/add")
    public LmyXlfResult<Boolean> add(@Validated({Insert.class}) @RequestBody ReqSysDictType reqSysDictType) {

        return LmyXlfResult.ok(sysDictTypeService.add(reqSysDictType));
    }

    /**
     * 修改字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典类型", businessType = BusinessTypeEnum.UPDATE)
    @PostMapping("/edit")
    public LmyXlfResult<Boolean> edit(@Validated({Update.class}) @RequestBody ReqSysDictType reqSysDictType) {

        return LmyXlfResult.ok(sysDictTypeService.edit(reqSysDictType));
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessTypeEnum.DELETE)
    @PostMapping("/remove")
    public LmyXlfResult<Boolean> remove(@Validated({Delete.class}) @RequestBody ReqSysDictType reqSysDictType) {

        return LmyXlfResult.ok(sysDictTypeService.remove(reqSysDictType));
    }

    /**
     * 刷新字典缓存
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessTypeEnum.CLEAN)
    @DeleteMapping("/refreshCache")
    public LmyXlfResult<Boolean> refreshCache() {

        return LmyXlfResult.ok(sysDictTypeService.refreshCache());
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionselect")
    public LmyXlfResult<List<RespSysDictType>> optionselect() {

        return LmyXlfResult.ok(sysDictTypeService.optionselect());
    }
}