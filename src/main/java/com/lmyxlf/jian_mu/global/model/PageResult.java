package com.lmyxlf.jian_mu.global.model;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description 使用 MybatisPlus 的分页
 * 快速创建的话直接用{@link #createPageResult(List)}
 * 分页实体类
 * total 总数
 * code  是否成功
 * data 当前页结果集
 * @since 17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = -296384968840178529L;
    private Long count;
    private int code;
    private List<T> data;

    /**
     * 返回分页对象
     *
     * @param list <b>必须是分页查询结果对象</b>
     * @return
     */
    @Deprecated
    public static PageResult<Object> createPageResult(List<Object> list) {
        PageInfo<Object> pageInfo = new PageInfo<>(list);
        return PageResult.builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build();
    }
}