package com.lmyxlf.jian_mu.global.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description 封装分页数据
 * @since 17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "分页数据")
public class PageData<T> implements Serializable {

    private static final long serialVersionUID = -275582248840137389L;

    public PageData(IPage<T> iPage) {
        this.records = iPage.getRecords();
        this.total = iPage.getTotal();
        this.size = iPage.getSize();
        this.current = iPage.getCurrent();
    }

    /**
     * 查询数据列表
     */
    @ApiModelProperty(value = "查询数据列表")
    private List<T> records;

    /**
     * 总数
     */
    @ApiModelProperty(value = "总数")
    private long total;
    /**
     * 每页显示条数，默认 10
     */
    @ApiModelProperty(value = "每页显示条数，默认 10")
    private long size;

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页")
    private long current;
}