package com.lmyxlf.jian_mu.business.batch_invitation.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/25 14:24
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@ApiModel("活码访问记录实体类")
public class LiveCodeHistory {

    @TableId
    @ApiModelProperty(value = "主键 id")
    private Integer id;

    @ApiModelProperty(value = "live_code 表主键 id")
    private Integer liveCodeId;

    @ApiModelProperty(value = "访问 ip")
    private String viewedIp;

    @ApiModelProperty(value = "访问地址")
    private String viewedAddress;

    @ApiModelProperty(value = "访问时间")
    private LocalDateTime viewedTime;
}