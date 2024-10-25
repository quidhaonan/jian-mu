package com.lmyxlf.jian_mu.admin.controller;

import com.lmyxlf.jian_mu.admin.annotation.Log;
import com.lmyxlf.jian_mu.admin.model.enums.BusinessTypeEnum;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysDictData;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysDictData;
import com.lmyxlf.jian_mu.admin.service.SysDictDataService;
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
 * @date 2024/9/18 23:12
 * @description
 * @since 17
 */
@RestController
@Api(tags = "数据字典信息")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.admin.urlPrefix}/system/dict/data")
public class SysDictDataController {

    private SysDictDataService sysDictDataService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @PostMapping("/list")
    public LmyXlfResult<PageData<RespSysDictData>> list(@RequestBody ReqSysDictData reqSysDictData) {

        return LmyXlfResult.ok(sysDictDataService.list(reqSysDictData));
    }

    @Log(title = "字典数据", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @PostMapping("/export")
    public void export(ReqSysDictData reqSysDictData, HttpServletResponse response) {

        sysDictDataService.export(reqSysDictData, response);
    }

    /**
     * 查询字典数据详细
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{id}")
    public LmyXlfResult<RespSysDictData> getInfo(@PathVariable Integer id) {

        return LmyXlfResult.ok(sysDictDataService.getInfo(id));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public LmyXlfResult<List<RespSysDictData>> dictType(@PathVariable String dictType) {

        return LmyXlfResult.ok(sysDictDataService.dictType(dictType));
    }

    /**
     * 新增字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典数据", businessType = BusinessTypeEnum.INSERT)
    @PostMapping("/add")
    public LmyXlfResult<Boolean> add(@Validated({Insert.class}) @RequestBody ReqSysDictData reqSysDictData) {

        return LmyXlfResult.ok(sysDictDataService.add(reqSysDictData));
    }

    /**
     * 修改保存字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典数据", businessType = BusinessTypeEnum.UPDATE)
    @PostMapping("/edit")
    public LmyXlfResult<Boolean> edit(@Validated({Update.class}) @RequestBody ReqSysDictData reqSysDictData) {

        return LmyXlfResult.ok(sysDictDataService.edit(reqSysDictData));
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessTypeEnum.DELETE)
    @PostMapping("/remove")
    public LmyXlfResult<Boolean> remove(@Validated({Delete.class}) @RequestBody ReqSysDictData reqSysDictData) {

        return LmyXlfResult.ok(sysDictDataService.remove(reqSysDictData));
    }
}