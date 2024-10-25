package com.lmyxlf.jian_mu.admin.controller;

import com.lmyxlf.jian_mu.admin.annotation.Log;
import com.lmyxlf.jian_mu.admin.model.enums.BusinessTypeEnum;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysDept;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysUser;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysUser;
import com.lmyxlf.jian_mu.admin.model.resp.RespTreeSelect;
import com.lmyxlf.jian_mu.admin.service.SysDeptService;
import com.lmyxlf.jian_mu.admin.service.SysUserService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/18 0:57
 * @description
 * @since 17
 */
@RestController
@Api(tags = "用户信息")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.admin.urlPrefix}/system/user")
public class SysUserController {

    private final SysUserService sysUserService;
    private final SysDeptService sysDeptService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @PostMapping("/list")
    public LmyXlfResult<PageData<RespSysUser>> list(@RequestBody ReqSysUser reqSysUser) {

        return LmyXlfResult.ok(new PageData<>(sysUserService.list(reqSysUser)));
    }

    @Log(title = "用户管理", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @PostMapping("/export")
    public void export(@RequestBody ReqSysUser reqSysUser, HttpServletResponse response) {

        sysUserService.export(reqSysUser, response);
    }

    @Log(title = "用户管理", businessType = BusinessTypeEnum.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    public LmyXlfResult<String> importData(MultipartFile file, boolean updateSupport) throws Exception {

        return LmyXlfResult.ok(sysUserService.importData(file, updateSupport));
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {

        sysUserService.importTemplate(response);
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = {"/", "/{id}"})
    public LmyXlfResult<RespSysUser> getInfo(@PathVariable(value = "id", required = false) Integer id) {

        return LmyXlfResult.ok(sysUserService.getInfo(id));
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessTypeEnum.INSERT)
    @PostMapping("/add")
    public LmyXlfResult<Boolean> add(@Validated({Insert.class}) @RequestBody ReqSysUser reqSysUser) {

        return LmyXlfResult.ok(sysUserService.add(reqSysUser));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessTypeEnum.UPDATE)
    @PostMapping("/edit")
    public LmyXlfResult<Boolean> edit(@Validated({Update.class}) @RequestBody ReqSysUser reqSysUser) {

        return LmyXlfResult.ok(sysUserService.edit(reqSysUser));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessTypeEnum.DELETE)
    @PostMapping("/remove")
    public LmyXlfResult<Boolean> remove(@Validated({Delete.class}) @RequestBody ReqSysUser reqSysUser) {

        return LmyXlfResult.ok(sysUserService.remove(reqSysUser));
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "用户管理", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping("/resetPwd")
    public LmyXlfResult<Boolean> resetPwd(@RequestBody ReqSysUser reqSysUser) {

        return LmyXlfResult.ok(sysUserService.resetPwd(reqSysUser));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping("/changeStatus")
    public LmyXlfResult<Boolean> changeStatus(@RequestBody ReqSysUser reqSysUser) {

        return LmyXlfResult.ok(sysUserService.changeStatus(reqSysUser));
    }

    /**
     * 根据用户编号获取授权角色
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping("/authRole/{id}")
    public LmyXlfResult<RespSysUser> authRole(@PathVariable("id") Integer id) {

        return LmyXlfResult.ok(sysUserService.authRole(id));
    }

    /**
     * 用户授权角色
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessTypeEnum.GRANT)
    @PostMapping("/authRole")
    public LmyXlfResult<Boolean> insertAuthRole(@RequestBody ReqSysUser reqSysUser) {

        return LmyXlfResult.ok(sysUserService.insertAuthRole(reqSysUser));
    }

    /**
     * 获取部门树列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/deptTree")
    public LmyXlfResult<List<RespTreeSelect>> deptTree(@RequestBody ReqSysDept reqSysDept) {

        return LmyXlfResult.ok(sysDeptService.selectDeptTreeList(reqSysDept));
    }
}