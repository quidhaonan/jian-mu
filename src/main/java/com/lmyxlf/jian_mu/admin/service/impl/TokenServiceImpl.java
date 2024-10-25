package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.lmyxlf.jian_mu.admin.constant.AdminConstant;
import com.lmyxlf.jian_mu.admin.constant.CacheConstant;
import com.lmyxlf.jian_mu.admin.model.dto.LoginUserDTO;
import com.lmyxlf.jian_mu.admin.service.TokenService;
import com.lmyxlf.jian_mu.global.util.IPUtils;
import com.lmyxlf.jian_mu.global.util.RedisUtil;
import com.lmyxlf.jian_mu.global.util.SpringMvcUtil;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/26 1:05
 * @description
 * @since 17
 */
@Slf4j
@Service("tokenService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TokenServiceImpl implements TokenService {

    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（默认 30 分钟）
    @Value("${token.expireTime}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;
    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;
    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Override
    public LoginUserDTO getLoginUser(HttpServletRequest request) {

        // 获取请求携带的令牌
        String token = getToken(request);
        if (StrUtil.isNotEmpty(token)) {

            try {

                Claims claims = parseToken(token);
                // 解析对应的权限以及用户信息
                String uuid = (String) claims.get(AdminConstant.LOGIN_USER_KEY);
                String userKey = getTokenKey(uuid);
                return RedisUtil.get(userKey);
            } catch (Exception e) {

                log.error("获取用户信息异常'{}'", e.getMessage());
            }
        }

        return null;
    }

    @Override
    public void setLoginUser(LoginUserDTO loginUserDTO) {

        if (ObjUtil.isNotNull(loginUserDTO) && StrUtil.isNotEmpty(loginUserDTO.getToken())) {

            refreshToken(loginUserDTO);
        }
    }

    @Override
    public void delLoginUser(String token) {

        if (StrUtil.isNotEmpty(token)) {

            String userKey = getTokenKey(token);
            RedisUtil.delete(userKey);
        }
    }

    @Override
    public String createToken(LoginUserDTO loginUserDTO) {

        String token = UUID.fastUUID().toString(true);
        loginUserDTO.setToken(token);
        setUserAgent(loginUserDTO);
        refreshToken(loginUserDTO);

        Map<String, Object> claims = new HashMap<>();
        claims.put(AdminConstant.LOGIN_USER_KEY, token);

        return createToken(claims);
    }

    @Override
    public void verifyToken(LoginUserDTO loginUserDTO) {

        long expireTime = loginUserDTO.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {

            refreshToken(loginUserDTO);
        }
    }

    @Override
    public void refreshToken(LoginUserDTO loginUserDTO) {

        loginUserDTO.setLoginTime(System.currentTimeMillis());
        loginUserDTO.setExpireTime(loginUserDTO.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据 uuid 将 loginUser 缓存
        String userKey = getTokenKey(loginUserDTO.getToken());
        RedisUtil.set(userKey, loginUserDTO, expireTime, TimeUnit.MINUTES);
    }

    @Override
    public void setUserAgent(LoginUserDTO loginUserDTO) {

        UserAgent userAgent = UserAgent.parseUserAgentString(Objects.requireNonNull(SpringMvcUtil.getRequest().orElse(null)).getHeader("User-Agent"));
        String ip = IPUtils.getIpAddr();
        loginUserDTO.setLoginIp(ip);
        loginUserDTO.setLoginLocation(IPUtils.getAddressByIp(ip));
        loginUserDTO.setBrowserType(userAgent.getBrowser().getName());
        loginUserDTO.setOs(userAgent.getOperatingSystem().getName());
    }

    @Override
    public String getUsernameFromToken(String token) {

        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request) {

        String token = request.getHeader(header);
        if (StrUtil.isNotEmpty(token) && token.startsWith(AdminConstant.TOKEN_PREFIX)) {

            token = token.replace(AdminConstant.TOKEN_PREFIX, "");
        }

        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {

        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private String getTokenKey(String uuid) {

        return CacheConstant.LOGIN_TOKEN_KEY + uuid;
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims) {

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
}