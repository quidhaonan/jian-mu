package com.lmyxlf.jian_mu.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.admin.dao.SysUserPostDao;
import com.lmyxlf.jian_mu.admin.model.entity.SysUserPost;
import com.lmyxlf.jian_mu.admin.service.SysUserPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/30 19:39
 * @description
 * @since 17
 */
@Slf4j
@Service("sysUserPostService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysUserPostServiceImpl extends ServiceImpl<SysUserPostDao, SysUserPost> implements SysUserPostService {

}