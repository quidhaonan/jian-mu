package com.lmyxlf.jian_mu.admin.model.req;

import com.lmyxlf.jian_mu.global.validation.group.Other;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/22 1:06
 * @description 用户登录对象
 * @since 17
 */
@Data
@Accessors(chain = true)
public class ReqLoginBody {

    /**
     * 用户名
     */
    @NotBlank(message = "用户账号不能为空", groups = {Other.class})
    @Size(min = 5, max = 30, message = "账户长度必须在 5 到 20 个字符之间")
    private String username;

    /**
     * 用户密码
     */
    @NotBlank(message = "用户密码不能为空", groups = {Other.class})
    @Size(min = 5, max = 20, message = "密码长度必须在 5 到 20 个字符之间")
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid;
}