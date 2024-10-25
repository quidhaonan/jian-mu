package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.admin.constant.UserConstant;
import com.lmyxlf.jian_mu.admin.dao.SysDeptDao;
import com.lmyxlf.jian_mu.admin.model.dto.SysUserDTO;
import com.lmyxlf.jian_mu.admin.model.entity.SysDept;
import com.lmyxlf.jian_mu.admin.model.entity.SysRole;
import com.lmyxlf.jian_mu.admin.model.entity.SysUser;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysDept;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysDept;
import com.lmyxlf.jian_mu.admin.model.resp.RespTreeSelect;
import com.lmyxlf.jian_mu.admin.service.SysDeptService;
import com.lmyxlf.jian_mu.admin.service.SysRoleService;
import com.lmyxlf.jian_mu.admin.service.SysUserService;
import com.lmyxlf.jian_mu.admin.util.SecurityUtil;
import com.lmyxlf.jian_mu.global.constant.DBConstant;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import com.lmyxlf.jian_mu.global.util.SpringContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/17 2:05
 * @description
 * @since 17
 */
@Slf4j
@Service("sysDeptService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao, SysDept> implements SysDeptService {

    private final SysDeptDao sysDeptDao;

    @Override
    public List<RespSysDept> list(ReqSysDept reqSysDept) {

        List<SysDept> sysDeptList = sysDeptDao.selectDeptList(reqSysDept);

        return sysDeptList.stream().map(item -> {
            RespSysDept respSysDept = new RespSysDept();
            BeanUtils.copyProperties(item, respSysDept);
            return respSysDept;
        }).collect(Collectors.toList());
    }

    @Override
    public List<RespSysDept> excludeChild(Integer id) {

        List<SysDept> sysDeptList = sysDeptDao.selectDeptList(new ReqSysDept());
        sysDeptList.removeIf(d -> d.getId().intValue() == id
                || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), id + ""));

        return sysDeptList.stream().map(item -> {
            RespSysDept respSysDept = new RespSysDept();
            BeanUtils.copyProperties(item, respSysDept);
            return respSysDept;
        }).collect(Collectors.toList());
    }

    @Override
    public RespSysDept getInfo(Integer id) {

        checkDeptDataScope(id);

        SysDept sysDept = sysDeptDao.selectDeptById(id);
        RespSysDept respSysDept = new RespSysDept();
        BeanUtils.copyProperties(sysDept, respSysDept);

        return respSysDept;
    }

    @Override
    public Boolean add(ReqSysDept reqSysDept) {

        Integer parentId = reqSysDept.getParentId();

        SysDept parentSysDept = this.lambdaQuery()
                .eq(SysDept::getDeptName, DBConstant.INITIAL_TIME)
                .eq(SysDept::getId, parentId)
                .one();
        // 如果父节点不为正常状态，则不允许新增子节点
        if (ObjUtil.isNull(parentSysDept) || !UserConstant.NORMAL.equals(parentSysDept.getStatus())) {

            log.warn("新增部门，父节点不为正常状态,不允许新增，reqSysDept：{}", reqSysDept);
            throw new LmyXlfException("部门停用，不允许新增");
        }
        reqSysDept.setAncestors(parentSysDept.getAncestors() + "," + reqSysDept.getParentId());

        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(reqSysDept, sysDept);
        return this.save(sysDept);
    }

    @Override
    public Boolean edit(ReqSysDept reqSysDept) {

        Integer id = reqSysDept.getId();
        Integer parentId = reqSysDept.getParentId();
        String deptName = reqSysDept.getDeptName();
        Integer status = reqSysDept.getStatus();

        checkDeptDataScope(id);
        if (ObjUtil.isNotNull(id) && id.equals(reqSysDept.getParentId())) {

            log.warn("修改部门，‘{}’失败，上级部门不能是自己", deptName);
            throw new LmyXlfException("修改部门'" + deptName + "'失败，上级部门不能是自己");
        } else if (UserConstant.DISABLE.equals(status) && sysDeptDao.selectNormalChildrenDeptById(id) > 0) {

            log.warn("该部门包含未停用的子部门：{}", deptName);
            throw new LmyXlfException("该部门包含未停用的子部门");
        }

        SysDept newParentDept = sysDeptDao.selectDeptById(parentId);
        SysDept oldDept = sysDeptDao.selectDeptById(id);
        if (ObjUtil.isNotNull(newParentDept) && ObjUtil.isNotNull(oldDept)) {

            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getId();
            String oldAncestors = oldDept.getAncestors();
            reqSysDept.setAncestors(newAncestors);
            updateDeptChildren(id, newAncestors, oldAncestors);
        }
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(reqSysDept, sysDept);
        boolean result = this.updateById(sysDept);
        if (UserConstant.NORMAL.equals(status) && StrUtil.isNotEmpty(reqSysDept.getAncestors())
                && !StrUtil.equals("0", reqSysDept.getAncestors())) {

            // 如果该部门是启用状态，则启用该部门的所有上级部门
            String ancestors = reqSysDept.getAncestors();
            Integer[] intArray = Convert.toIntArray(ancestors);
            this.lambdaUpdate()
                    .set(SysDept::getStatus, UserConstant.NORMAL)
                    .in(SysDept::getId, (Object) intArray)
                    .eq(SysDept::getDeleteTime, DBConstant.INITIAL_TIME)
                    .update();
        }

        return Boolean.TRUE;
    }

    @Override
    public Boolean remove(Integer id) {

        Integer deptCount = this.lambdaQuery()
                .eq(SysDept::getParentId, id)
                .eq(SysDept::getDeleteTime, DBConstant.INITIAL_TIME)
                .count();
        if (deptCount > 0) {

            log.warn("存在下级部门,不允许删除，id：{}", id);
            throw new LmyXlfException("存在下级部门,不允许删除");
        }

        Integer userCount = SpringContextHolder.getBean(SysUserService.class).lambdaQuery()
                .eq(SysUser::getDeptId, id)
                .eq(SysUser::getDeleteTime, DBConstant.INITIAL_TIME)
                .count();
        if (userCount > 0) {

            log.warn("部门存在用户,不允许删除，id：{}", id);
            throw new LmyXlfException("部门存在用户,不允许删除");
        }

        checkDeptDataScope(id);

        return this.lambdaUpdate()
                .set(SysDept::getDeleteTime, LocalDateTimeUtil.now())
                .eq(SysDept::getId, id)
                .eq(SysDept::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();
    }

    @Override
    public List<Integer> selectDeptListByRoleId(Integer id) {

        SysRole sysRole = SpringContextHolder.getBean(SysRoleService.class).lambdaQuery()
                .eq(SysRole::getId, id)
                .eq(SysRole::getDeleteTime, DBConstant.INITIAL_TIME)
                .one();
        if (ObjUtil.isNull(sysRole)) {

            log.warn("角色不存在，查询部门树信息失败，id：{}", id);
            return Collections.emptyList();
        }

        return sysDeptDao.selectDeptListByRoleId(sysRole.getId(), sysRole.getDeptCheckStrictly());
    }

    @Override
    public List<RespTreeSelect> selectDeptTreeList(ReqSysDept reqSysDept) {

        List<SysDept> sysDeptList = sysDeptDao.selectDeptList(reqSysDept);
        List<RespSysDept> respSysDeptList = sysDeptList.stream().map(item -> {
            RespSysDept respSysDept = new RespSysDept();
            BeanUtils.copyProperties(item, respSysDept);
            return respSysDept;
        }).toList();

        return buildDeptTreeSelect(respSysDeptList);
    }

    @Override
    public void checkDeptDataScope(Integer id) {

        if (!SysUserDTO.isAdmin(SecurityUtil.getUserId()) && ObjUtil.isNotNull(id)) {

            ReqSysDept reqSysDept = new ReqSysDept();
            reqSysDept.setId(id);
            List<SysDept> sysDeptList = sysDeptDao.selectDeptList(reqSysDept);
            if (CollUtil.isEmpty(sysDeptList)) {

                throw new LmyXlfException("没有权限访问部门数据");
            }
        }
    }

    /**
     * 修改子元素关系
     *
     * @param id           被修改的部门 id
     * @param newAncestors 新的父 id 集合
     * @param oldAncestors 旧的父 id 集合
     */
    public void updateDeptChildren(Integer id, String newAncestors, String oldAncestors) {

        List<SysDept> children = sysDeptDao.selectChildrenDeptById(id);
        for (SysDept child : children) {

            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }

        if (CollUtil.isNotEmpty(children)) {

            this.updateBatchById(children);
        }
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param respSysDeptList 部门列表
     * @return 下拉树结构列表
     */
    public List<RespTreeSelect> buildDeptTreeSelect(List<RespSysDept> respSysDeptList) {

        List<RespSysDept> deptTrees = buildDeptTree(respSysDeptList);
        return deptTrees.stream().map(RespTreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 构建前端所需要树结构
     *
     * @param respSysDeptList 部门列表
     * @return 树结构列表
     */
    public List<RespSysDept> buildDeptTree(List<RespSysDept> respSysDeptList) {

        List<RespSysDept> returnList = new ArrayList<>();
        List<Integer> tempList = respSysDeptList.stream().map(RespSysDept::getId).toList();
        for (RespSysDept respSysDept : respSysDeptList) {

            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(respSysDept.getParentId())) {

                recursionFn(respSysDeptList, respSysDept);
                returnList.add(respSysDept);
            }
        }
        if (returnList.isEmpty()) {

            returnList = respSysDeptList;
        }

        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<RespSysDept> respSysDeptList, RespSysDept respSysDept) {

        // 得到子节点列表
        List<RespSysDept> childList = getChildList(respSysDeptList, respSysDept);
        respSysDept.setChildren(childList);
        for (RespSysDept tChild : childList) {

            if (hasChild(respSysDeptList, tChild)) {

                recursionFn(respSysDeptList, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<RespSysDept> getChildList(List<RespSysDept> respSysDeptList, RespSysDept respSysDept) {

        List<RespSysDept> tlist = new ArrayList<>();
        for (RespSysDept item : respSysDeptList) {

            if (ObjUtil.isNotNull(item.getParentId()) &&
                    item.getParentId().intValue() == respSysDept.getId().intValue()) {

                tlist.add(item);
            }
        }

        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<RespSysDept> respSysDeptList, RespSysDept respSysDept) {

        return !getChildList(respSysDeptList, respSysDept).isEmpty();
    }
}