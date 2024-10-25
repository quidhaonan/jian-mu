package com.lmyxlf.jian_mu.admin.model.resp;

import com.lmyxlf.jian_mu.admin.model.dto.SysUserDTO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/1 0:28
 * @description 个人信息返回体
 * @since 17
 */
@Data
@Accessors(chain = true)
public class RespPersonalInfo {

    private SysUserDTO info;

    private String roleGroup;

    private String postGroup;
}