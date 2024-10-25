package com.lmyxlf.jian_mu.admin.service;

import com.lmyxlf.jian_mu.admin.model.req.ReqSysUser;
import com.lmyxlf.jian_mu.admin.model.resp.RespPersonalInfo;
import com.lmyxlf.jian_mu.admin.model.resp.RespUploadAvatar;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/1 0:36
 * @description
 * @since 17
 */
public interface SysProfileService {

    RespPersonalInfo profile();

    Boolean updateProfile(ReqSysUser reqSysUser);

    Boolean updatePwd(String oldPassword, String newPassword);

    RespUploadAvatar avatar(MultipartFile file);
}