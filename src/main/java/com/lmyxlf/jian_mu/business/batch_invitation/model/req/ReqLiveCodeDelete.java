package com.lmyxlf.jian_mu.business.batch_invitation.model.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/25 4:51
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "删除活码请求体")
public class ReqLiveCodeDelete {

    @NotNull(message = "id 不能为空")
    private Integer id;
}