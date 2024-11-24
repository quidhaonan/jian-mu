package com.lmyxlf.jian_mu.business.batch_invitation.model.req;

import com.lmyxlf.jian_mu.global.model.PageEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/11/9 23:55
 * @description ftp 活码请求体
 * @since 17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ReqLiveCodeFtp extends PageEntity {

    @NotNull(message = "id 不能为空")
    private Integer id;

    /**
     * 活码名称
     */
    private String name;

    /**
     * 状态，0：正常，1：关闭
     */
    private Integer status;
}