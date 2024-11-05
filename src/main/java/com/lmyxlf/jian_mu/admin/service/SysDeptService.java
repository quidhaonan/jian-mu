package com.lmyxlf.jian_mu.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lmyxlf.jian_mu.admin.model.entity.SysDept;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysDept;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysDept;
import com.lmyxlf.jian_mu.admin.model.resp.RespTreeSelect;

import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/17 2:05
 * @description
 * @since 17
 */
public interface SysDeptService extends IService<SysDept> {

    List<RespSysDept> list(ReqSysDept reqSysDept);

    List<RespSysDept> excludeChild(Integer id);

    RespSysDept getInfo(Integer id);

    Boolean add(ReqSysDept reqSysDept);

    Boolean edit(ReqSysDept reqSysDept);

    Boolean remove(Integer id);

    /**
     * 根据角色 id 查询部门树信息
     *
     * @param id 角色 id
     * @return 选中部门列表
     */
    List<Integer> selectDeptListByRoleId(Integer id);

    /**
     * 查询部门树结构信息
     *
     * @return 部门树信息集合
     */
    List<RespTreeSelect> selectDeptTreeList();

    /**
     * 校验部门是否有数据权限
     *
     * @param id 部门 id
     */
    void checkDeptDataScope(Integer id);
}