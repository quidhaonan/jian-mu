package com.lmyxlf.jian_mu.admin.controller;

import com.lmyxlf.jian_mu.admin.annotation.Log;
import com.lmyxlf.jian_mu.admin.model.enums.BusinessTypeEnum;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysUser;
import com.lmyxlf.jian_mu.admin.model.resp.RespPersonalInfo;
import com.lmyxlf.jian_mu.admin.model.resp.RespUploadAvatar;
import com.lmyxlf.jian_mu.admin.service.SysProfileService;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import com.lmyxlf.jian_mu.global.validation.group.Other;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/1 0:17
 * @description
 * @since 17
 */
@RestController
@Api(tags = "个人信息，业务处理")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.admin.urlPrefix}/system/user/profile")
public class SysProfileController {

    private final SysProfileService sysProfileService;

    /**
     * 个人信息
     */
    @GetMapping
    public LmyXlfResult<RespPersonalInfo> profile() {

        return LmyXlfResult.ok(sysProfileService.profile());
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessTypeEnum.UPDATE)
    @PostMapping("/edit")
    public LmyXlfResult<Boolean> updateProfile(@Validated({Other.class}) @RequestBody ReqSysUser reqSysUser) {

        return LmyXlfResult.ok(sysProfileService.updateProfile(reqSysUser));
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping("/updatePwd")
    public LmyXlfResult<Boolean> updatePwd(String oldPassword, String newPassword) {

        return LmyXlfResult.ok(sysProfileService.updatePwd(oldPassword, newPassword));
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessTypeEnum.UPDATE)
    @PostMapping("/avatar")
    public LmyXlfResult<RespUploadAvatar> avatar(@RequestParam("avatarfile") MultipartFile file) throws Exception {

        return LmyXlfResult.ok(sysProfileService.avatar(file));
    }
}