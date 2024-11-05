package com.lmyxlf.jian_mu.admin.controller;

import com.lmyxlf.jian_mu.admin.annotation.Log;
import com.lmyxlf.jian_mu.admin.dao.SysUserDao;
import com.lmyxlf.jian_mu.admin.model.enums.BusinessTypeEnum;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysRole;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysUser;
import com.lmyxlf.jian_mu.admin.model.resp.RespDeptTree;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysRole;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysUser;
import com.lmyxlf.jian_mu.admin.service.SysRoleService;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import com.lmyxlf.jian_mu.global.model.PageData;
import com.lmyxlf.jian_mu.global.validation.group.Delete;
import com.lmyxlf.jian_mu.global.validation.group.Insert;
import com.lmyxlf.jian_mu.global.validation.group.Other;
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
 * @date 2024/10/1 14:59
 * @description
 * @since 17
 */
@RestController
@Api(tags = "角色信息")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.admin.urlPrefix}/system/role")
public class SysRoleController {

    private final SysRoleService sysRoleService;
    private final SysUserDao sysUserDao;

    @PreAuthorize("@permissionService.hasPermi('system:role:list')")
    @PostMapping("/list")
    public LmyXlfResult<PageData<RespSysRole>> list(@RequestBody ReqSysRole reqSysRole) {

        return LmyXlfResult.ok(new PageData<>(sysRoleService.list(reqSysRole)));
    }

    @Log(title = "角色管理", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@permissionService.hasPermi('system:role:export')")
    @PostMapping("/export")
    public void export(@RequestBody ReqSysRole reqSysRole, HttpServletResponse response) {

        sysRoleService.export(reqSysRole, response);
    }

    /**
     * 根据角色编号获取详细信息
     */
    @PreAuthorize("@permissionService.hasPermi('system:role:query')")
    @GetMapping(value = "/{id}")
    public LmyXlfResult<RespSysRole> getInfo(@PathVariable Integer id) {

        return LmyXlfResult.ok(sysRoleService.getInfo(id));
    }

    /**
     * 新增角色
     */
    @PreAuthorize("@permissionService.hasPermi('system:role:add')")
    @Log(title = "角色管理", businessType = BusinessTypeEnum.INSERT)
    @PostMapping("/add")
    public LmyXlfResult<Boolean> add(@Validated({Insert.class}) @RequestBody ReqSysRole reqSysRole) {

        return LmyXlfResult.ok(sysRoleService.add(reqSysRole));
    }

    /**
     * 修改保存角色
     */
    @PreAuthorize("@permissionService.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessTypeEnum.UPDATE)
    @PostMapping("/edit")
    public LmyXlfResult<Boolean> edit(@Validated({Update.class}) @RequestBody ReqSysRole reqSysRole) {

        return LmyXlfResult.ok(sysRoleService.edit(reqSysRole));
    }

    /**
     * 修改保存数据权限
     */
    @PreAuthorize("@permissionService.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping("/dataScope")
    public LmyXlfResult<Boolean> dataScope(@RequestBody ReqSysRole reqSysRole) {

        return LmyXlfResult.ok(sysRoleService.dataScope(reqSysRole));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@permissionService.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping("/changeStatus")
    public LmyXlfResult<Boolean> changeStatus(@RequestBody ReqSysRole reqSysRole) {

        return LmyXlfResult.ok(sysRoleService.changeStatus(reqSysRole));
    }

    /**
     * 删除角色
     */
    @PreAuthorize("@permissionService.hasPermi('system:role:remove')")
    @Log(title = "角色管理", businessType = BusinessTypeEnum.DELETE)
    @PostMapping("/remove")
    public LmyXlfResult<Boolean> remove(@Validated({Delete.class}) @RequestBody ReqSysRole reqSysRole) {

        return LmyXlfResult.ok(sysRoleService.remove(reqSysRole));
    }

    /**
     * 获取角色选择框列表
     */
    @PreAuthorize("@permissionService.hasPermi('system:role:query')")
    @GetMapping("/optionselect")
    public LmyXlfResult<List<RespSysRole>> optionselect() {

        return LmyXlfResult.ok(sysRoleService.optionselect());
    }

    /**
     * 查询已分配用户角色列表
     */
    @PreAuthorize("@permissionService.hasPermi('system:role:list')")
    @GetMapping("/authUser/allocatedList")
    public LmyXlfResult<PageData<RespSysUser>> allocatedList(ReqSysUser reqSysUser) {

        return LmyXlfResult.ok(new PageData<>(sysRoleService.allocatedList(reqSysUser)));
    }

    /**
     * 查询未分配用户角色列表
     */
    @PreAuthorize("@permissionService.hasPermi('system:role:list')")
    @GetMapping("/authUser/unallocatedList")
    public LmyXlfResult<PageData<RespSysUser>> unallocatedList(ReqSysUser reqSysUser) {

        return LmyXlfResult.ok(new PageData<>(sysRoleService.unallocatedList(reqSysUser)));
    }

    /**
     * 取消授权用户
     */
    @PreAuthorize("@permissionService.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessTypeEnum.GRANT)
    @PutMapping("/authUser/cancel")
    public LmyXlfResult<Boolean> cancelAuthUser(@RequestBody ReqSysRole reqSysRole) {

        return LmyXlfResult.ok(sysRoleService.cancelAuthUser(reqSysRole));
    }

    /**
     * 批量取消授权用户
     */
    @PreAuthorize("@permissionService.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessTypeEnum.GRANT)
    @PutMapping("/authUser/cancelAll")
    public LmyXlfResult<Boolean> cancelAuthUserAll(@RequestBody ReqSysRole reqSysRole) {

        return LmyXlfResult.ok(sysRoleService.cancelAuthUserAll(reqSysRole));
    }

    /**
     * 批量选择用户授权
     */
    @PreAuthorize("@permissionService.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessTypeEnum.GRANT)
    @PutMapping("/authUser/selectAll")
    public LmyXlfResult<Boolean> selectAuthUserAll(@Validated({Other.class}) @RequestBody ReqSysRole reqSysRole) {

        return LmyXlfResult.ok(sysRoleService.selectAuthUserAll(reqSysRole));
    }

    /**
     * 获取对应角色部门树列表
     */
    @PreAuthorize("@permissionService.hasPermi('system:role:query')")
    @GetMapping(value = "/deptTree/{id}")
    public LmyXlfResult<RespDeptTree> deptTree(@PathVariable("id") Integer id) {

        return LmyXlfResult.ok(sysRoleService.deptTree(id));
    }
}