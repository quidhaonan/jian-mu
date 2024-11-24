package com.lmyxlf.jian_mu.business.batch_invitation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lmyxlf.jian_mu.business.batch_invitation.model.entity.LiveCodeFtp;
import com.lmyxlf.jian_mu.business.batch_invitation.model.req.ReqLiveCodeDelete;
import com.lmyxlf.jian_mu.business.batch_invitation.model.req.ReqLiveCodeFtp;
import com.lmyxlf.jian_mu.business.batch_invitation.model.resp.RespLiveCodeFtp;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/24 1:05
 * @description
 * @since 17
 */
public interface LiveCodeFtpService extends IService<LiveCodeFtp> {

    /**
     * 获得多个活码
     *
     * @param reqLiveCodeFtp
     * @return
     */
    IPage<RespLiveCodeFtp> list(ReqLiveCodeFtp reqLiveCodeFtp);

    /**
     * 添加活码
     *
     * @param file
     * @param remark
     * @return
     */
    Boolean add(MultipartFile file, String remark);

    /**
     * 获得单个活码
     *
     * @param id
     * @param response
     */
    void getOne(Integer id, HttpServletResponse response);

    /**
     * 修改活码
     *
     * @param file
     * @param id
     * @param remark
     * @return
     */
    Boolean update(MultipartFile file, Integer id, String remark);

    /**
     * 删除活码
     *
     * @param reqLiveCodeDelete
     * @return
     */
    Boolean delete(ReqLiveCodeDelete reqLiveCodeDelete);

    /**
     * 获得城市
     *
     * @param region 格式：中国|0|重庆|重庆市|移动
     * @return 将 0 值去除，并用 - 连接，即：中国-重庆-重庆市-移动
     */
    String getFullAddress(String region);
}