package com.lmyxlf.jian_mu.business.batch_invitation.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.business.batch_invitation.constant.BIConstant;
import com.lmyxlf.jian_mu.business.batch_invitation.constant.CrossBranchConstant;
import com.lmyxlf.jian_mu.business.batch_invitation.dao.LiveCodeLocalDao;
import com.lmyxlf.jian_mu.business.batch_invitation.model.entity.LiveCodeHistory;
import com.lmyxlf.jian_mu.business.batch_invitation.model.entity.LiveCodeLocal;
import com.lmyxlf.jian_mu.business.batch_invitation.model.enums.LiveCodeStatusEnum;
import com.lmyxlf.jian_mu.business.batch_invitation.model.req.ReqLiveCodeLocal;
import com.lmyxlf.jian_mu.business.batch_invitation.model.resp.RespLiveCodeLocal;
import com.lmyxlf.jian_mu.business.batch_invitation.service.LiveCodeFtpService;
import com.lmyxlf.jian_mu.business.batch_invitation.service.LiveCodeHistoryService;
import com.lmyxlf.jian_mu.business.batch_invitation.service.LiveCodeLocalService;
import com.lmyxlf.jian_mu.global.constant.CODE_MSG;
import com.lmyxlf.jian_mu.global.constant.DBConstant;
import com.lmyxlf.jian_mu.global.constant.LmyXlfReqParamConstant;
import com.lmyxlf.jian_mu.global.constant.SysConstant;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import com.lmyxlf.jian_mu.global.handler.ThreadPoolsHandler;
import com.lmyxlf.jian_mu.global.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/11/10 1:52
 * @description
 * @since 17
 */
@Slf4j
@Service("liveCodeLocalService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LiveCodeLocalServiceImpl extends ServiceImpl<LiveCodeLocalDao, LiveCodeLocal> implements LiveCodeLocalService {

    @Value("${server.port}")
    private Integer port;
    @Value("${jian_mu.batch_invitation.urlPrefix}")
    private String urlPrefix;
    private String requestUrl = "/liveCode/local/pub/";

    /**
     * 本地活码操作路径
     */
    private static final String LIVE_CODE_LOCAL_PATH = SysConstant.USER_DIR +
            BIConstant.BATCH_INVITATION_PATH + "/live_code_local/";
    private final LiveCodeHistoryService liveCodeHistoryService;
    private final LiveCodeFtpService liveCodeFtpService;

    @Override
    public IPage<RespLiveCodeLocal> list(ReqLiveCodeLocal reqLiveCodeLocal) {
        log.info("获得本地活码列表，reqLiveCodeLocal：{}", reqLiveCodeLocal);

        Integer page = reqLiveCodeLocal.getPage();
        Integer size = reqLiveCodeLocal.getSize();
        String name = reqLiveCodeLocal.getName();
        Integer status = reqLiveCodeLocal.getStatus();
        LocalDateTime beginTime = reqLiveCodeLocal.getBeginTime();
        LocalDateTime endTime = reqLiveCodeLocal.getEndTime();
        Integer userId = MethodInvokerUtil.invoke(CrossBranchConstant.SECURITY_UTIL,
                CrossBranchConstant.SECURITY_UTIL_GET_USER_ID,
                new Class<?>[]{});

        LambdaQueryChainWrapper<LiveCodeLocal> liveCodeLocalLambdaQueryChainWrapper = this.lambdaQuery()
                .like(StrUtil.isNotEmpty(name), LiveCodeLocal::getName, name)
                .eq(ObjUtil.isNotNull(status), LiveCodeLocal::getStatus, status)
                .ge(ObjUtil.isNotNull(beginTime), LiveCodeLocal::getCreateTime, beginTime)
                .le(ObjUtil.isNotNull(endTime), LiveCodeLocal::getCreateTime, endTime)
                .eq(LiveCodeLocal::getUserId, userId)
                .eq(LiveCodeLocal::getDeleteTime, DBConstant.INITIAL_TIME);

        IPage<LiveCodeLocal> liveCodeLocalPage = this.page(
                new Page<>(page, size), liveCodeLocalLambdaQueryChainWrapper.getWrapper());

        log.info("获得本地活码列表，liveCodeLocalList：{}", liveCodeLocalPage.getRecords());
        return liveCodeLocalPage.convert(item -> {

            RespLiveCodeLocal respLiveCodeLocal = new RespLiveCodeLocal();
            BeanUtils.copyProperties(item, respLiveCodeLocal);
            respLiveCodeLocal
                    .setStatus(String.valueOf(item.getStatus()))
                    .setUrl(IPUtils.getLocalIp() + ":" + port + urlPrefix + requestUrl + item.getRandomStr());
            return respLiveCodeLocal;
        });
    }

    @Override
    public RespLiveCodeLocal getInfo(Integer id) {
        log.info("获得本地活码信息，id：{}", id);

        Integer userId = MethodInvokerUtil.invoke(CrossBranchConstant.SECURITY_UTIL,
                CrossBranchConstant.SECURITY_UTIL_GET_USER_ID,
                new Class<?>[]{});

        LiveCodeLocal liveCodeLocal = this.lambdaQuery()
                .eq(LiveCodeLocal::getId, id)
                .eq(LiveCodeLocal::getUserId, userId)
                .eq(LiveCodeLocal::getDeleteTime, DBConstant.INITIAL_TIME)
                .one();
        if (ObjUtil.isNull(liveCodeLocal)) {

            log.warn("获得活码失败，活码不存在，id：{}，userId：{}，liveCodeLocal：{}",
                    id, userId, liveCodeLocal);
            throw new LmyXlfException("活码不存在");
        }

        RespLiveCodeLocal respLiveCodeLocal = new RespLiveCodeLocal();
        BeanUtils.copyProperties(liveCodeLocal, respLiveCodeLocal);
        respLiveCodeLocal.setStatus(String.valueOf(liveCodeLocal.getStatus()));

        return respLiveCodeLocal;
    }

    @Override
    public void getOneById(Integer id, HttpServletResponse response) {
        log.info("获得本地活码，id：{}", id);

        Integer userId = MethodInvokerUtil.invoke(CrossBranchConstant.SECURITY_UTIL,
                CrossBranchConstant.SECURITY_UTIL_GET_USER_ID,
                new Class<?>[]{});

        LiveCodeLocal liveCodeLocal = this.lambdaQuery()
                .eq(LiveCodeLocal::getId, id)
                .eq(LiveCodeLocal::getUserId, userId)
                .eq(LiveCodeLocal::getDeleteTime, DBConstant.INITIAL_TIME)
                .one();
        if (ObjUtil.isNull(liveCodeLocal)) {

            log.warn("获得活码失败，活码不存在，id：{}，userId：{}，liveCodeLocal：{}",
                    id, userId, liveCodeLocal);
            throw new LmyXlfException("活码不存在");
        }

        String fileName = liveCodeLocal.getFileName();
        try {

            FileUtil.writeBytes(LIVE_CODE_LOCAL_PATH + fileName, response.getOutputStream());
        } catch (IOException e) {

            log.error("获得本地活码失败，id：{}，userId：{}，liveCodeLocal：{}",
                    id, userId, liveCodeLocal);
            throw new LmyXlfException(CODE_MSG.ERROR);
        }
    }

    @Override
    public void getOneByRandomStr(String randomStr, HttpServletResponse response) {
        log.info("获得本地活码，randomStr：{}", randomStr);

        LiveCodeLocal liveCodeLocal = this.lambdaQuery()
                .eq(LiveCodeLocal::getRandomStr, randomStr)
                .eq(LiveCodeLocal::getStatus, DBConstant.STATUS_NORMAL)
                .eq(LiveCodeLocal::getDeleteTime, DBConstant.INITIAL_TIME)
                .one();
        if (ObjUtil.isNull(liveCodeLocal) || !LiveCodeStatusEnum.NORMAL.getValue().equals(liveCodeLocal.getStatus())) {

            log.warn("获得活码失败，活码不存在或未启用，randomStr：{}，liveCodeLocal：{}",
                    randomStr, liveCodeLocal);
            throw new LmyXlfException("活码不存在或未启用");
        }

        String fileName = liveCodeLocal.getFileName();
        try {

            FileUtil.writeBytes(LIVE_CODE_LOCAL_PATH + fileName, response.getOutputStream());
        } catch (IOException e) {

            log.error("获得本地活码失败，randomStr：{}，liveCodeLocal：{}",
                    randomStr, liveCodeLocal);
            throw new LmyXlfException(CODE_MSG.ERROR);
        }

        // 记录访问信息
        liveCodeLocal.setViewedCount(liveCodeLocal.getViewedCount() + 1);
        this.updateById(liveCodeLocal);
        // 获得访问地址
        String region = IPUtils.getAddressByIp(MDC.get(LmyXlfReqParamConstant.REMOTE_IP));
        LiveCodeHistory liveCodeHistory = new LiveCodeHistory();
        liveCodeHistory
                .setLiveCodeId(liveCodeLocal.getId())
                .setViewedIp(MDC.get(LmyXlfReqParamConstant.REMOTE_IP))
                .setViewedAddress(liveCodeFtpService.getFullAddress(region));
        liveCodeHistoryService.save(liveCodeHistory);
    }

    @Override
    public Boolean add(ReqLiveCodeLocal reqLiveCodeLocal, MultipartFile file) {
        log.info("添加本地活码，reqLiveCodeLocal：{}，file：{}", reqLiveCodeLocal, file.getOriginalFilename());

        String name = reqLiveCodeLocal.getName();
        Integer status = reqLiveCodeLocal.getStatus();
        String remark = reqLiveCodeLocal.getRemark();
        String originalFilename = file.getOriginalFilename();
        if (!ImageUtil.isImage(file)) {

            log.warn("添加活码失败，不是图片");
            throw new LmyXlfException("需上传图片");
        }

        // 获得最终文件名
        String randomStr = RandomUtil.generateRandomStr(9);
        String fileName = FileUtil.upload(LIVE_CODE_LOCAL_PATH, file);

        Integer userId = MethodInvokerUtil.invoke(CrossBranchConstant.SECURITY_UTIL,
                CrossBranchConstant.SECURITY_UTIL_GET_USER_ID,
                new Class<?>[]{});
        LiveCodeLocal liveCodeLocal = new LiveCodeLocal();
        liveCodeLocal
                .setName(name)
                .setUserId(userId)
                .setFileName(fileName)
                .setFileOriginalName(originalFilename)
                .setFileSize(file.getSize())
                .setRandomStr(randomStr)
                .setStatus(status)
                .setRemark(remark);

        return this.save(liveCodeLocal);
    }

    @Override
    public Boolean update(ReqLiveCodeLocal reqLiveCodeLocal, MultipartFile file) {
        log.info("修改本地活码，reqLiveCodeLocal：{}，file：{}，fileSize：{}",
                reqLiveCodeLocal, file.getOriginalFilename(), file.getSize());

        Integer id = reqLiveCodeLocal.getId();
        String name = reqLiveCodeLocal.getName();
        String remark = reqLiveCodeLocal.getRemark();
        String fileName = null, fileOriginalName = null;
        Long fileSize = null;
        Integer userId = MethodInvokerUtil.invoke(CrossBranchConstant.SECURITY_UTIL,
                CrossBranchConstant.SECURITY_UTIL_GET_USER_ID,
                new Class<?>[]{});
        if (!ImageUtil.isImage(file)) {

            log.warn("添加活码失败，不是图片");
            throw new LmyXlfException("需上传图片");
        }

        LiveCodeLocal liveCodeLocal = this.lambdaQuery()
                .eq(LiveCodeLocal::getId, id)
                .eq(LiveCodeLocal::getUserId, userId)
                .eq(LiveCodeLocal::getDeleteTime, DBConstant.INITIAL_TIME)
                .one();
        if (ObjUtil.isNull(liveCodeLocal)) {

            log.warn("该活码不存在，修改失败，reqLiveCodeLocal：{}，file：{}，fileSize：{}",
                    reqLiveCodeLocal, file.getOriginalFilename(), file.getSize());
            throw new LmyXlfException("活码不存在");
        }

        if (!file.isEmpty()) {

            String fileNameDB = liveCodeLocal.getFileName();
            FileUtil.deleteFile(LIVE_CODE_LOCAL_PATH + fileNameDB);
            fileName = FileUtil.upload(LIVE_CODE_LOCAL_PATH, file);
            fileOriginalName = file.getOriginalFilename();
            fileSize = file.getSize();
        }

        liveCodeLocal
                .setName(StrUtil.isNotEmpty(name) ? name : liveCodeLocal.getName())
                .setRemark(StrUtil.isNotEmpty(remark) ? remark : liveCodeLocal.getRemark())
                .setFileName(StrUtil.isNotEmpty(fileName) ? fileName : liveCodeLocal.getFileName())
                .setFileOriginalName(StrUtil.isNotEmpty(fileOriginalName) ? fileOriginalName : liveCodeLocal.getFileOriginalName())
                .setFileSize(ObjUtil.isNotNull(fileSize) ? fileSize : liveCodeLocal.getFileSize());
        return this.updateById(liveCodeLocal);
    }

    @Override
    public Boolean changeStatus(ReqLiveCodeLocal reqLiveCodeLocal) {
        log.info("修改本地活码状态，reqLiveCodeLocal：{}", reqLiveCodeLocal);

        Integer id = reqLiveCodeLocal.getId();
        Integer status = reqLiveCodeLocal.getStatus();
        Integer userId = MethodInvokerUtil.invoke(CrossBranchConstant.SECURITY_UTIL,
                CrossBranchConstant.SECURITY_UTIL_GET_USER_ID,
                new Class<?>[]{});

        return this.lambdaUpdate()
                .set(LiveCodeLocal::getStatus, status)
                .eq(LiveCodeLocal::getId, id)
                .eq(LiveCodeLocal::getUserId, userId)
                .eq(LiveCodeLocal::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();
    }

    @Override
    @Transactional
    public Boolean remove(ReqLiveCodeLocal reqLiveCodeLocal) {
        log.info("删除本地活码，reqLiveCodeLocal：{}", reqLiveCodeLocal);

        List<Integer> ids = reqLiveCodeLocal.getIds();
        Integer userId = MethodInvokerUtil.invoke(CrossBranchConstant.SECURITY_UTIL,
                CrossBranchConstant.SECURITY_UTIL_GET_USER_ID,
                new Class<?>[]{});
        List<LiveCodeLocal> liveCodeLocalList = this.lambdaQuery()
                .in(LiveCodeLocal::getId, ids)
                .eq(LiveCodeLocal::getUserId, userId)
                .eq(LiveCodeLocal::getDeleteTime, DBConstant.INITIAL_TIME)
                .list();
        if (CollUtil.isEmpty(liveCodeLocalList)) {

            log.info("删除本地活码失败，reqLiveCodeLocal：{}，userId：{}", reqLiveCodeLocal, ids);
            throw new LmyXlfException("无该活码");
        }

        ThreadPoolsHandler.ASYNC_SCHEDULED_POOL.execute(() -> {

            liveCodeLocalList.forEach(item -> {

                FileUtil.deleteFile(LIVE_CODE_LOCAL_PATH + item.getFileName());
                item.setDeleteTime(LocalDateTimeUtil.now());
            });

            this.updateBatchById(liveCodeLocalList);
        });
        return Boolean.TRUE;
    }
}