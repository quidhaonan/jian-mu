package com.lmyxlf.jian_mu.admin.controller;

import com.lmyxlf.jian_mu.admin.annotation.Log;
import com.lmyxlf.jian_mu.admin.model.enums.BusinessTypeEnum;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysConfig;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysConfig;
import com.lmyxlf.jian_mu.admin.service.SysConfigService;
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
 * @date 2024/9/15 3:03
 * @description
 * @since 17
 */
@RestController
@Api(tags = "参数配置 信息操作处理")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.admin.urlPrefix}/system/config")
public class SysConfigController {

    private final SysConfigService sysConfigService;

    /**
     * 获取参数配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @PostMapping("/list")
    public LmyXlfResult<PageData<RespSysConfig>> list(@RequestBody ReqSysConfig reqSysConfig) {

        return LmyXlfResult.ok(new PageData<>(sysConfigService.list(reqSysConfig)));
    }

    @Log(title = "参数管理", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:config:export')")
    @PostMapping("/export")
    public void export(@RequestBody ReqSysConfig reqSysConfig, HttpServletResponse response) {

        sysConfigService.export(reqSysConfig, response);
    }

    /**
     * 根据参数编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping(value = "/{id}")
    public LmyXlfResult<RespSysConfig> getInfo(@PathVariable("id") Integer id) {

        return LmyXlfResult.ok(sysConfigService.selectConfigById(id));
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    public LmyXlfResult<String> getConfigKey(@PathVariable("configKey") String configKey) {

        return LmyXlfResult.ok(sysConfigService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @Log(title = "参数管理", businessType = BusinessTypeEnum.INSERT)
    @PostMapping("/add")
    public LmyXlfResult<Boolean> add(@Validated({Insert.class}) @RequestBody ReqSysConfig reqSysConfig) {

        return LmyXlfResult.ok(sysConfigService.add(reqSysConfig));
    }

    /**
     * 修改参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "参数管理", businessType = BusinessTypeEnum.UPDATE)
    @PostMapping("/edit")
    public LmyXlfResult<Boolean> edit(@Validated({Update.class}) @RequestBody ReqSysConfig reqSysConfig) {

        return LmyXlfResult.ok(sysConfigService.edit(reqSysConfig));
    }

    /**
     * 删除参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessTypeEnum.DELETE)
    @PostMapping("/remove")
    public LmyXlfResult<Boolean> remove(@Validated({Delete.class}) @RequestBody ReqSysConfig reqSysConfig) {

        return LmyXlfResult.ok(sysConfigService.remove(reqSysConfig));
    }

    /**
     * 刷新参数缓存
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessTypeEnum.CLEAN)
    @DeleteMapping("/refreshCache")
    public LmyXlfResult<Boolean> refreshCache() {

        return LmyXlfResult.ok(sysConfigService.refreshCache());
    }
}