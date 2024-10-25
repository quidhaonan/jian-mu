package com.lmyxlf.jian_mu.admin.model.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/7 14:23
 * @description
 * @since 17
 */
@Data
@ApiModel("登录用户身份权限")
@Accessors(chain = true)
public class LoginUserDTO implements UserDetails {

    @ApiModelProperty(value = "主键 id，即用户 id")
    private Integer id;

    @ApiModelProperty(value = "部门 id")
    private Integer deptId;

    @ApiModelProperty(value = "用户账号")
    private String token;

    @ApiModelProperty(value = "登录时间")
    private Long loginTime;

    @ApiModelProperty(value = "过期时间")
    private Long expireTime;

    @ApiModelProperty(value = "登录 ip 地址")
    private String loginIp;

    @ApiModelProperty(value = "登录地点")
    private String loginLocation;

    @ApiModelProperty(value = "浏览器类型")
    private String browserType;

    @ApiModelProperty(value = "操作系统")
    private String os;

    @ApiModelProperty(value = "权限列表")
    private Set<String> permissions;

    @ApiModelProperty(value = "用户信息")
    private SysUserDTO sysUserDTO;

    public LoginUserDTO() {

    }

    public LoginUserDTO(SysUserDTO sysUserDTO, Set<String> permissions) {

        this.sysUserDTO = sysUserDTO;
        this.permissions = permissions;
    }

    public LoginUserDTO(Integer userId, Integer deptId, SysUserDTO sysUserDTO, Set<String> permissions) {

        this.id = userId;
        this.deptId = deptId;
        this.sysUserDTO = sysUserDTO;
        this.permissions = permissions;
    }

    @Override
    @JSONField(serialize = false)
    public String getPassword() {

        return sysUserDTO.getPassword();
    }

    @Override
    public String getUsername() {

        return sysUserDTO.getUserName();
    }

    /**
     * 账户是否未过期，过期无法验证
     */
    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonExpired() {

        return true;
    }

    /**
     * 指定用户是否解锁，锁定的用户无法进行身份验证
     *
     * @return
     */
    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonLocked() {

        return true;
    }

    /**
     * 指示是否已过期的用户的凭据（密码），过期的凭据防止认证
     *
     * @return
     */
    @Override
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {

        return true;
    }

    /**
     * 是否可用，禁用的用户不能身份验证
     *
     * @return
     */
    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return null;
    }
}