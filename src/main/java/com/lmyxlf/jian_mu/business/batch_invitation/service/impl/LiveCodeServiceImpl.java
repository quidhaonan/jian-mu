package com.lmyxlf.jian_mu.business.batch_invitation.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.business.batch_invitation.dao.LiveCodeDao;
import com.lmyxlf.jian_mu.business.batch_invitation.model.entity.LiveCode;
import com.lmyxlf.jian_mu.business.batch_invitation.model.entity.LiveCodeHistory;
import com.lmyxlf.jian_mu.business.batch_invitation.model.enums.FtpClientType;
import com.lmyxlf.jian_mu.business.batch_invitation.model.enums.LiveCodeStatusEnum;
import com.lmyxlf.jian_mu.business.batch_invitation.model.req.ReqLiveCodeDelete;
import com.lmyxlf.jian_mu.business.batch_invitation.model.req.ReqLiveCodeList;
import com.lmyxlf.jian_mu.business.batch_invitation.model.resp.RespLiveCodeList;
import com.lmyxlf.jian_mu.business.batch_invitation.service.LiveCodeHistoryService;
import com.lmyxlf.jian_mu.business.batch_invitation.service.LiveCodeService;
import com.lmyxlf.jian_mu.business.batch_invitation.util.FtpClientBiUtil;
import com.lmyxlf.jian_mu.global.constant.CODE_MSG;
import com.lmyxlf.jian_mu.global.constant.DBConstant;
import com.lmyxlf.jian_mu.global.constant.LmyXlfReqParamConstant;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import com.lmyxlf.jian_mu.global.model.HttpResult;
import com.lmyxlf.jian_mu.global.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/24 1:06
 * @description
 * @since 17
 */
@Slf4j
@Service("liveCodeService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LiveCodeServiceImpl extends ServiceImpl<LiveCodeDao, LiveCode> implements LiveCodeService {

    @Value("${server.port}")
    private Integer port;
    @Value("${jian_mu.batch_invitation.urlPrefix}")
    private String urlPrefix;
    /**
     * 获得单个活码路径
     */
    private final String reqPath = "/liveCode/get/";
    private static final String BASE_PATH = "/liveCode";
    private final LiveCodeHistoryService liveCodeHistoryService;

    @Override
    public List<RespLiveCodeList> list(ReqLiveCodeList reqLiveCodeList) {
        log.info("获得活码列表，reqLiveCodeList：{}", reqLiveCodeList);

        String name = reqLiveCodeList.getName();
        Integer status = reqLiveCodeList.getStatus();
        List<LiveCode> liveCodeList = this.lambdaQuery()
                .eq(ObjUtil.isNotNull(name), LiveCode::getName, name)
                .eq(ObjUtil.isNotNull(status), LiveCode::getStatus, status)
                .list();

        // 活码 id 集合，查询活码访问记录（今日已查看记录）
        List<Integer> liveCodeIds = liveCodeList.stream()
                .map(LiveCode::getId)
                .collect(Collectors.toList());
        Map<Integer, Long> viewedTodayCountMap = liveCodeHistoryService.getViewedTodayCount(liveCodeIds);

        List<RespLiveCodeList> respLiveCodeList = liveCodeList.stream().map(item -> {
            RespLiveCodeList respLiveCode = new RespLiveCodeList();
            BeanUtils.copyProperties(item, respLiveCode);

            // 今日已查看次数以及服务器访问链接
            respLiveCode
                    .setViewedTodayCount(viewedTodayCountMap.getOrDefault(item.getId(), 0L))
                    .setServerUrl(IPUtils.getLocalIp() + ":" + port + urlPrefix + reqPath + item.getId());
            return respLiveCode;
        }).collect(Collectors.toList());

        log.info("获得活码列表，reqLiveCodeList：{}，respLiveCodeListList：{}", respLiveCodeList, respLiveCodeList);
        return respLiveCodeList;
    }

    @Override
    public Boolean add(MultipartFile file, String remark) {
        log.info("添加活码，file：{}，remark：{}", file.getOriginalFilename(), remark);

        if (!ImageUtil.isImage(file)) {
            log.warn("添加活码失败，不是图片");
            throw new LmyXlfException("需上传图片");
        }

        // 获得最终文件名
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.split("\\.")[1];
        String randomStr = RandomUtil.generateRandomStr(9);
        String fileName = randomStr + "." + suffix;

        LiveCode liveCode = new LiveCode();
        liveCode.setFtpClientId(FtpClientType.FTP_01.getType())
                .setUrl(FtpClientType.FTP_01.getDomain() + BASE_PATH + "/" + fileName)
                .setFileName(fileName)
                .setRandomStr(randomStr)
                .setFileSize(file.getSize())
                .setRemark(remark);
        this.save(liveCode);

        FTPClient ftpClient = FtpClientBiUtil.getFtpClient(FtpClientType.FTP_01, BASE_PATH);

        return FtpClientUtil.upload(ftpClient, file, fileName);
    }

    @Override
    public void get(Integer id, HttpServletResponse response) {
        log.info("获得活码，id：{}", id);

        LiveCode liveCode = this.lambdaQuery()
                .eq(LiveCode::getId, id)
                .eq(LiveCode::getIsDelete, DBConstant.NOT_DELETED)
                .one();
        if (ObjUtil.isNull(liveCode)) {
            log.warn("获得活码失败，活码不存在，id：{}", id);
            throw new LmyXlfException("活码不存在");
        }
        if (!LiveCodeStatusEnum.NORMAL.getValue().equals(liveCode.getStatus())) {
            log.warn("获得活码失败，活码未开启，id：{}", id);
            throw new LmyXlfException("活码未开启");
        }

        // 下载活码图片，格式为 byte[]
        String url = liveCode.getUrl();
        String fileName = liveCode.getFileName();
        HttpResult httpResult = LmyXlfHttp
                .get(url)
                .build()
                .bodyBytes();
        byte[] bytes = httpResult.getBytes();

        // 设置响应头，根据图片类型设置 MIME 类型
        response.setContentType("image/" + fileName.split("\\.")[1]);

        // 将图片数据写入响应
        try (ServletOutputStream out = response.getOutputStream()) {
            out.write(bytes);
            out.flush();
        } catch (IOException e) {
            log.error("活码写入失败，e：{}", e.getMessage());
            throw new LmyXlfException(CODE_MSG.ERROR);
        }

        // 记录访问信息
        liveCode.setViewedCount(liveCode.getViewedCount() + 1);
        this.updateById(liveCode);
        // 获得访问地址
        String region = IPUtils.getAddressByIp(MDC.get(LmyXlfReqParamConstant.REMOTE_IP));
        LiveCodeHistory liveCodeHistory = new LiveCodeHistory();
        liveCodeHistory
                .setLiveCodeId(liveCode.getId())
                .setViewedIp(MDC.get(LmyXlfReqParamConstant.REMOTE_IP))
                .setViewedAddress(getFullAddress(region));
        liveCodeHistoryService.save(liveCodeHistory);
    }

    @Override
    public Boolean update(MultipartFile file, Integer id, String remark) {
        log.info("修改活码，id：{}，remark：{}，file：{}", id, remark, file.getOriginalFilename());

        LiveCode liveCode = this.lambdaQuery()
                .eq(LiveCode::getId, id)
                .eq(LiveCode::getIsDelete, DBConstant.NOT_DELETED)
                .one();
        if (ObjUtil.isNull(liveCode)) {
            log.warn("修改活码失败，活码不存在，id：{}，file：{}，remark：{}", id, remark, file.getOriginalFilename());
            throw new LmyXlfException("活码不存在");
        }

        // 获得后缀，并生成文件名
        String randomStr = liveCode.getRandomStr();
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.split("\\.")[1];
        String fileName = randomStr + "." + suffix;

        Integer ftpClientId = liveCode.getFtpClientId();
        FTPClient ftpClient = FtpClientBiUtil.getFtpClient(ftpClientId, BASE_PATH);
        FtpClientUtil.upload(ftpClient, file, fileName);

        // 更新活码信息
        liveCode
                .setFileName(fileName)
                .setUrl(FtpClientType.FTP_01.getDomain() + BASE_PATH + "/" + fileName)
                .setFileSize(file.getSize())
                .setRemark(remark);
        this.updateById(liveCode);
        return Boolean.TRUE;
    }

    @Override
    public Boolean delete(ReqLiveCodeDelete reqLiveCodeDelete) {
        log.info("删除活码，reqLiveCodeDelete：{}", reqLiveCodeDelete);

        Integer id = reqLiveCodeDelete.getId();
        LiveCode liveCode = this.lambdaQuery()
                .eq(LiveCode::getId, id)
                .eq(LiveCode::getIsDelete, DBConstant.NOT_DELETED)
                .one();
        if (ObjUtil.isNull(liveCode)) {
            log.warn("删除活码失败，活码不存在，reqLiveCodeDelete：{}", reqLiveCodeDelete);
            throw new LmyXlfException("活码不存在");
        }

        // 删除 FTP 文件
        String fileName = liveCode.getFileName();
        Integer ftpClientId = liveCode.getFtpClientId();
        FTPClient ftpClient = FtpClientBiUtil.getFtpClient(ftpClientId, BASE_PATH);
        FtpClientUtil.deleteFile(ftpClient, fileName);

        // 修改数据库
        liveCode.setIsDelete(DBConstant.DELETED);
        this.updateById(liveCode);

        return Boolean.TRUE;
    }

    /**
     * 获得城市
     *
     * @param region 格式：中国|0|重庆|重庆市|移动
     * @return 将 0 值去除，并用 - 连接，即：中国-重庆-重庆市-移动
     */
    public String getFullAddress(String region) {
        log.info("获得城市，region：{}", region);

        if (StrUtil.isEmpty(region)) {
            log.warn("获得城市失败，region：{}，返回默认城市：未知", region);
            return "未知";
        }
        // 用 | 进行分割
        String[] split = region.split("\\|");
        StringBuilder result = new StringBuilder();
        for (String item : split) {
            if (!"0".equals(item)) {
                result.append(item).append("-");
            }
        }
        // 去除最后的 -
        if (result.length() > 0) {
            result.setLength(result.length() - 1);

            log.info("成功获得城市，city：{}，region：{}", result.toString(), region);
            return result.toString();
        }

        log.warn("获得城市失败，region：{}，返回默认城市：未知", region);
        return "未知";
    }
}