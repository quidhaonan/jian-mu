package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.admin.dao.SysLoginInfoDao;
import com.lmyxlf.jian_mu.admin.model.entity.SysLoginInfo;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysLoginInfo;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysLoginInfo;
import com.lmyxlf.jian_mu.admin.service.SysLoginInfoService;
import com.lmyxlf.jian_mu.admin.util.ExcelUtil;
import com.lmyxlf.jian_mu.global.constant.DBConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/7 2:18
 * @description
 * @since 17
 */
@Slf4j
@Service("sysLoginInfoService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysLoginInfoServiceImpl extends ServiceImpl<SysLoginInfoDao, SysLoginInfo> implements SysLoginInfoService {

    @Override
    public Page<RespSysLoginInfo> list(ReqSysLoginInfo reqSysLoginInfo) {

        Integer page = reqSysLoginInfo.getPage();
        Integer size = reqSysLoginInfo.getSize();
        String userName = reqSysLoginInfo.getUserName();
        String loginIp = reqSysLoginInfo.getLoginIp();
        Integer status = reqSysLoginInfo.getStatus();
        LocalDateTime beginTime = reqSysLoginInfo.getBeginTime();
        LocalDateTime endTime = reqSysLoginInfo.getEndTime();

        LambdaQueryChainWrapper<SysLoginInfo> sysLoginInfoLambdaQueryChainWrapper = this.lambdaQuery()
                .eq(SysLoginInfo::getDeleteTime, DBConstant.INITIAL_TIME)
                .like(StrUtil.isNotEmpty(userName), SysLoginInfo::getUserName, userName)
                .like(StrUtil.isNotEmpty(loginIp), SysLoginInfo::getLoginIp, loginIp)
                .eq(ObjUtil.isNotNull(status), SysLoginInfo::getStatus, status)
                .ge(ObjUtil.isNotNull(beginTime), SysLoginInfo::getLoginTime, beginTime)
                .le(ObjUtil.isNotNull(endTime), SysLoginInfo::getLoginLocation, endTime)
                .orderByDesc(SysLoginInfo::getLoginTime);

        Page<SysLoginInfo> sysLoginInfoPage = this.page(
                new Page<>(page, size), sysLoginInfoLambdaQueryChainWrapper.getWrapper());


        // 仅为将返回对象转为 Resp
        List<RespSysLoginInfo> records = sysLoginInfoPage.getRecords().stream()
                .map(item -> {
                    RespSysLoginInfo respSysLoginInfo = new RespSysLoginInfo();
                    BeanUtils.copyProperties(item, respSysLoginInfo);
                    return respSysLoginInfo;
                })
                .collect(Collectors.toList());
        Page<RespSysLoginInfo> result = new Page<>(
                sysLoginInfoPage.getCurrent(), sysLoginInfoPage.getSize(), sysLoginInfoPage.getTotal());
        result.setRecords(records);

        return result;
    }

    @Override
    public void export(ReqSysLoginInfo reqSysLoginInfo, HttpServletResponse response) {

        String userName = reqSysLoginInfo.getUserName();
        String loginIp = reqSysLoginInfo.getLoginIp();
        Integer status = reqSysLoginInfo.getStatus();
        LocalDateTime beginTime = reqSysLoginInfo.getBeginTime();
        LocalDateTime endTime = reqSysLoginInfo.getEndTime();

        List<SysLoginInfo> list = this.lambdaQuery()
                .eq(SysLoginInfo::getDeleteTime, DBConstant.INITIAL_TIME)
                .like(StrUtil.isNotEmpty(userName), SysLoginInfo::getUserName, userName)
                .like(StrUtil.isNotEmpty(loginIp), SysLoginInfo::getLoginIp, loginIp)
                .eq(ObjUtil.isNotNull(status), SysLoginInfo::getStatus, status)
                .ge(ObjUtil.isNotNull(beginTime), SysLoginInfo::getLoginTime, beginTime)
                .le(ObjUtil.isNotNull(endTime), SysLoginInfo::getLoginLocation, endTime)
                .orderByDesc(SysLoginInfo::getLoginTime)
                .list();

        ExcelUtil<SysLoginInfo> util = new ExcelUtil<>(SysLoginInfo.class);
        util.exportExcel(response, list, "登录日志");
    }

    @Override
    public Boolean remove(ReqSysLoginInfo reqSysLoginInfo) {

        List<Integer> ids = reqSysLoginInfo.getIds();

        return this.lambdaUpdate()
                .eq(SysLoginInfo::getDeleteTime, DBConstant.INITIAL_TIME)
                .set(SysLoginInfo::getDeleteTime, DateUtil.now())
                .in(SysLoginInfo::getId, ids)
                .update();
    }

    @Override
    public Boolean clean() {

        return this.lambdaUpdate()
                .eq(SysLoginInfo::getDeleteTime, DBConstant.INITIAL_TIME)
                .set(SysLoginInfo::getDeleteTime, DateUtil.now())
                .update();
    }
}