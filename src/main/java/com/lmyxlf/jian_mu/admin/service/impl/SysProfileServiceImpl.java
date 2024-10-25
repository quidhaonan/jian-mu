package com.lmyxlf.jian_mu.admin.service.impl;

import com.lmyxlf.jian_mu.admin.config.AdminConfig;
import com.lmyxlf.jian_mu.admin.model.dto.LoginUserDTO;
import com.lmyxlf.jian_mu.admin.model.dto.SysUserDTO;
import com.lmyxlf.jian_mu.admin.model.entity.SysUser;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysUser;
import com.lmyxlf.jian_mu.admin.model.resp.RespPersonalInfo;
import com.lmyxlf.jian_mu.admin.model.resp.RespUploadAvatar;
import com.lmyxlf.jian_mu.admin.service.SysProfileService;
import com.lmyxlf.jian_mu.admin.service.SysUserService;
import com.lmyxlf.jian_mu.admin.service.TokenService;
import com.lmyxlf.jian_mu.admin.util.SecurityUtil;
import com.lmyxlf.jian_mu.global.constant.DBConstant;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import com.lmyxlf.jian_mu.global.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/1 0:37
 * @description
 * @since 17
 */
@Slf4j
@Service("sysProfileService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysProfileServiceImpl implements SysProfileService {

    private final SysUserService sysUserService;
    private final TokenService tokenService;

    @Override
    public RespPersonalInfo profile() {

        RespPersonalInfo result = new RespPersonalInfo();

        LoginUserDTO loginUserDTO = SecurityUtil.getLoginUser();
        SysUserDTO user = loginUserDTO.getSysUserDTO();

        String roleGroup = sysUserService.selectUserRoleGroup(loginUserDTO.getUsername());
        String postGroup = sysUserService.selectUserPostGroup(loginUserDTO.getUsername());

        result
                .setInfo(user)
                .setRoleGroup(roleGroup)
                .setPostGroup(postGroup);
        return result;
    }

    @Override
    public Boolean updateProfile(ReqSysUser reqSysUser) {

        String nickName = reqSysUser.getNickName();
        String phoneNumber = reqSysUser.getPhoneNumber();
        String email = reqSysUser.getEmail();
        Integer sex = reqSysUser.getSex();

        LoginUserDTO loginUserDTO = SecurityUtil.getLoginUser();
        SysUserDTO currentUser = loginUserDTO.getSysUserDTO();
        currentUser.setNickName(nickName);
        currentUser.setPhoneNumber(phoneNumber);
        currentUser.setEmail(email);
        currentUser.setSex(sex);

        boolean update = sysUserService.lambdaUpdate()
                .set(SysUser::getNickName, nickName)
                .set(SysUser::getPhoneNumber, phoneNumber)
                .set(SysUser::getEmail, email)
                .set(SysUser::getSex, sex)
                .eq(SysUser::getId, currentUser.getId())
                .eq(SysUser::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();
        if (update) {

            // 更新缓存用户信息
            tokenService.setLoginUser(loginUserDTO);
            return Boolean.TRUE;
        }

        throw new LmyXlfException("修改个人信息异常，请联系管理员");
    }

    @Override
    public Boolean updatePwd(String oldPassword, String newPassword) {

        LoginUserDTO loginUserDTO = SecurityUtil.getLoginUser();
        Integer id = loginUserDTO.getId();
        String password = loginUserDTO.getPassword();

        if (!SecurityUtil.matchesPassword(oldPassword, password)) {

            log.warn("修改失败，旧密码错误，oldPassword：{}，newPassword：{}，loginUserDTO：{}",
                    oldPassword, newPassword, loginUserDTO);
            throw new LmyXlfException("修改失败，旧密码错误");
        }
        if (SecurityUtil.matchesPassword(newPassword, password)) {

            log.warn("新密码不能与旧密码相同，oldPassword：{}，newPassword：{}，loginUserDTO：{}",
                    oldPassword, newPassword, loginUserDTO);
            throw new LmyXlfException("新密码不能与旧密码相同");
        }

        newPassword = SecurityUtil.encryptPassword(newPassword);
        boolean update = sysUserService.lambdaUpdate()
                .set(SysUser::getPassword, newPassword)
                .eq(SysUser::getId, id)
                .eq(SysUser::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();
        if (update) {

            // 更新缓存用户密码
            loginUserDTO.getSysUserDTO().setPassword(newPassword);
            tokenService.setLoginUser(loginUserDTO);
            return Boolean.TRUE;
        }

        throw new LmyXlfException("修改密码异常，请联系管理员");
    }

    @Override
    public RespUploadAvatar avatar(MultipartFile file) {

        if (!file.isEmpty()) {

            LoginUserDTO loginUserDTO = SecurityUtil.getLoginUser();
            String avatar = null;
            try {

                avatar = FileUtil.upload(AdminConfig.getAvatarPath(), file, FileUtil.IMAGE_EXTENSION);
                boolean update = sysUserService.lambdaUpdate()
                        .set(SysUser::getAvatar, avatar)
                        .eq(SysUser::getId, loginUserDTO.getId())
                        .eq(SysUser::getDeleteTime, DBConstant.INITIAL_TIME)
                        .update();

                if (update) {

                    RespUploadAvatar respUploadAvatar = new RespUploadAvatar();
                    respUploadAvatar.setImgUrl(avatar);
                    // 更新缓存用户头像
                    loginUserDTO.getSysUserDTO().setAvatar(avatar);
                    tokenService.setLoginUser(loginUserDTO);
                    return respUploadAvatar;
                }
            } catch (IOException e) {

                log.error("上传图片异常，fileName：{}，fileSize：{}，e：{}",
                        file.getOriginalFilename(), file.getSize(), e.getMessage());
                throw new LmyXlfException("上传图片异常，请联系管理员");
            }
        }

        throw new LmyXlfException("上传图片异常，请联系管理员");
    }
}