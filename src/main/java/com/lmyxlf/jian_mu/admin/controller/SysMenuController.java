package com.lmyxlf.jian_mu.admin.controller;

import com.lmyxlf.jian_mu.admin.annotation.Log;
import com.lmyxlf.jian_mu.admin.model.enums.BusinessTypeEnum;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysMenu;
import com.lmyxlf.jian_mu.admin.model.resp.RespRoleMenuTree;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysMenu;
import com.lmyxlf.jian_mu.admin.model.resp.RespTreeSelect;
import com.lmyxlf.jian_mu.admin.service.SysMenuService;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import com.lmyxlf.jian_mu.global.validation.group.Insert;
import com.lmyxlf.jian_mu.global.validation.group.Update;
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
 * @date 2024/9/29 5:27
 * @description
 * @since 17
 */
@RestController
@Api(tags = "菜单信息")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.admin.urlPrefix}/system/menu")
public class SysMenuController {

    private SysMenuService sysMenuService;

    /**
     * 获取菜单列表
     */
    @PreAuthorize("@permissionService.hasPermi('system:menu:list')")
    @PostMapping("/list")
    public LmyXlfResult<List<RespSysMenu>> list(@RequestBody ReqSysMenu reqSysMenu) {

        return LmyXlfResult.ok(sysMenuService.list(reqSysMenu));
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @PreAuthorize("@permissionService.hasPermi('system:menu:query')")
    @GetMapping(value = "/{id}")
    public LmyXlfResult<RespSysMenu> getInfo(@PathVariable Integer id) {

        return LmyXlfResult.ok(sysMenuService.getInfo(id));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public LmyXlfResult<List<RespTreeSelect>> treeselect(ReqSysMenu reqSysMenu) {

        return LmyXlfResult.ok(sysMenuService.treeselect(reqSysMenu));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public LmyXlfResult<RespRoleMenuTree> roleMenuTreeselect(@PathVariable("roleId") Integer roleId) {

        return LmyXlfResult.ok(sysMenuService.roleMenuTreeselect(roleId));
    }

    /**
     * 新增菜单
     */
    @PreAuthorize("@permissionService.hasPermi('system:menu:add')")
    @Log(title = "菜单管理", businessType = BusinessTypeEnum.INSERT)
    @PostMapping("/add")
    public LmyXlfResult<Boolean> add(@Validated({Insert.class}) @RequestBody ReqSysMenu reqSysMenu) {

        return LmyXlfResult.ok(sysMenuService.add(reqSysMenu));
    }

    /**
     * 修改菜单
     */
    @PreAuthorize("@permissionService.hasPermi('system:menu:edit')")
    @Log(title = "菜单管理", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping("/edit")
    public LmyXlfResult<Boolean> edit(@Validated({Update.class}) @RequestBody ReqSysMenu reqSysMenu) {

        return LmyXlfResult.ok(sysMenuService.edit(reqSysMenu));
    }

    /**
     * 删除菜单
     */
    @PreAuthorize("@permissionService.hasPermi('system:menu:remove')")
    @Log(title = "菜单管理", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/{id}")
    public LmyXlfResult<Boolean> remove(@PathVariable("id") Integer id) {

        return LmyXlfResult.ok(sysMenuService.remove(id));
    }
}