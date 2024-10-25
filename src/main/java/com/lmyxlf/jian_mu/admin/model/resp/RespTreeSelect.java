package com.lmyxlf.jian_mu.admin.model.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/13 21:30
 * @description Treeselect 树结构实体类
 * @since 17
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class RespTreeSelect {

    /**
     * 节点 id
     */
    private Integer id;

    /**
     * 节点名称
     */
    private String label;

    /**
     * 子节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<RespTreeSelect> children;

    public RespTreeSelect(RespSysDept respSysDept) {

        this.id = respSysDept.getId();
        this.label = respSysDept.getDeptName();
        this.children = respSysDept.getChildren().stream().map(RespTreeSelect::new).collect(Collectors.toList());
    }

    public RespTreeSelect(RespSysMenu respSysMenu) {

        this.id = respSysMenu.getId();
        this.label = respSysMenu.getMenuName();
        this.children = respSysMenu.getChildren().stream().map(RespTreeSelect::new).collect(Collectors.toList());
    }
}