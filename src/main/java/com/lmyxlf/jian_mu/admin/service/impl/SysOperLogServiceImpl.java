package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.admin.dao.SysOperLogDao;
import com.lmyxlf.jian_mu.admin.model.entity.SysOperLog;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysOperLog;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysOperLog;
import com.lmyxlf.jian_mu.admin.service.SysOperLogService;
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
 * @date 2024/9/14 0:16
 * @description
 * @since 17
 */
@Slf4j
@Service("sysOperLogService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogDao, SysOperLog> implements SysOperLogService {

    @Override
    public Page<RespSysOperLog> list(ReqSysOperLog reqSysOperLog) {

        Integer page = reqSysOperLog.getPage();
        Integer size = reqSysOperLog.getSize();
        String title = reqSysOperLog.getTitle();
        List<Integer> businessType = reqSysOperLog.getBusinessType();
        String operName = reqSysOperLog.getOperName();
        String operIp = reqSysOperLog.getOperIp();
        Integer status = reqSysOperLog.getStatus();
        LocalDateTime beginTime = reqSysOperLog.getBeginTime();
        LocalDateTime endTime = reqSysOperLog.getEndTime();

        LambdaQueryChainWrapper<SysOperLog> sysOperLogLambdaQueryChainWrapper = this.lambdaQuery()
                .eq(SysOperLog::getDeleteTime, DBConstant.INITIAL_TIME)
                .like(StrUtil.isNotEmpty(title), SysOperLog::getTitle, title)
                .in(CollUtil.isNotEmpty(businessType), SysOperLog::getBusinessType, businessType)
                .like(StrUtil.isNotEmpty(operName), SysOperLog::getOperName, operName)
                .like(StrUtil.isNotEmpty(operIp), SysOperLog::getOperIp, operIp)
                .eq(ObjUtil.isNotNull(status), SysOperLog::getStatus, status)
                .ge(ObjUtil.isNotNull(beginTime), SysOperLog::getOperTime, beginTime)
                .le(ObjUtil.isNotNull(endTime), SysOperLog::getOperTime, endTime)
                .orderByDesc(SysOperLog::getId);

        Page<SysOperLog> sysOperLogPage = this.page(
                new Page<>(page, size), sysOperLogLambdaQueryChainWrapper);


        // 仅为将返回对象转为 Resp
        List<RespSysOperLog> records = sysOperLogPage.getRecords().stream()
                .map(item -> {
                    RespSysOperLog respSysOperLog = new RespSysOperLog();
                    BeanUtils.copyProperties(item, respSysOperLog);
                    return respSysOperLog;
                })
                .collect(Collectors.toList());
        Page<RespSysOperLog> result = new Page<>(
                sysOperLogPage.getCurrent(), sysOperLogPage.getSize(), sysOperLogPage.getTotal());
        result.setRecords(records);

        return result;
    }

    @Override
    public void export(ReqSysOperLog reqSysOperLog, HttpServletResponse response) {

        String title = reqSysOperLog.getTitle();
        List<Integer> businessType = reqSysOperLog.getBusinessType();
        String operName = reqSysOperLog.getOperName();
        String operIp = reqSysOperLog.getOperIp();
        Integer status = reqSysOperLog.getStatus();
        LocalDateTime beginTime = reqSysOperLog.getBeginTime();
        LocalDateTime endTime = reqSysOperLog.getEndTime();

        List<SysOperLog> list = this.lambdaQuery()
                .eq(SysOperLog::getDeleteTime, DBConstant.INITIAL_TIME)
                .like(StrUtil.isNotEmpty(title), SysOperLog::getTitle, title)
                .in(CollUtil.isNotEmpty(businessType), SysOperLog::getBusinessType, businessType)
                .like(StrUtil.isNotEmpty(operName), SysOperLog::getOperName, operName)
                .like(StrUtil.isNotEmpty(operIp), SysOperLog::getOperIp, operIp)
                .eq(ObjUtil.isNotNull(status), SysOperLog::getStatus, status)
                .ge(ObjUtil.isNotNull(beginTime), SysOperLog::getOperTime, beginTime)
                .le(ObjUtil.isNotNull(endTime), SysOperLog::getOperTime, endTime)
                .orderByDesc(SysOperLog::getId)
                .list();

        ExcelUtil<SysOperLog> util = new ExcelUtil<>(SysOperLog.class);
        util.exportExcel(response, list, "操作日志");
    }

    @Override
    public Boolean remove(ReqSysOperLog reqSysOperLog) {

        List<Integer> ids = reqSysOperLog.getIds();

        return this.lambdaUpdate()
                .eq(SysOperLog::getDeleteTime, DBConstant.INITIAL_TIME)
                .set(SysOperLog::getDeleteTime, DateUtil.now())
                .in(SysOperLog::getId, ids)
                .update();
    }

    @Override
    public Boolean clean() {

        return this.lambdaUpdate()
                .eq(SysOperLog::getDeleteTime, DBConstant.INITIAL_TIME)
                .set(SysOperLog::getDeleteTime, DateUtil.now())
                .update();
    }
}