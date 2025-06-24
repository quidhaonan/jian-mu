package com.lmyxlf.jian_mu.business.batch_invitation.util;

import com.lmyxlf.jian_mu.business.batch_invitation.model.dto.YeZiAddBlacklist;
import com.lmyxlf.jian_mu.business.batch_invitation.model.dto.YeZiGetMessageDto;
import com.lmyxlf.jian_mu.business.batch_invitation.model.dto.YeZiGetMobileDto;
import com.lmyxlf.jian_mu.business.batch_invitation.model.dto.YeZiLoginDto;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import com.lmyxlf.jian_mu.global.util.LmyXlfHttp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2025/6/22 14:53
 * @description 椰子接码平台工具类（第三方服务，参数不作校验）
 * @since 17
 */
@Slf4j
@Component
public class YeZiSMSUtil {

    // 用户名
    @Value("${yezi_sms.username}")
    private String USERNAME;

    // 密码
    @Value("${yezi_sms.password}")
    private String PASSWORD;

    // token
    public static String YEZI_TOKEN;

    // 主域名
    private static final String MAIN_DOMAIN = "http://api.sqhyw.net:90";

    // 备用域名
    private static final String ALTERNATIVE_DOMAIN = "http://api.nnanx.com:90";

    // 登录成功标识
    private static final String LOGIN_SUCCESS_MSG = "登录成功";

    // 成功标识
    private static final String SUCCESS_MSG = "ok";

    // 登录 URL
    private static final String LOGIN_URL = "/api/logins";

    // 用户取卡 URL
    private static final String GET_MOBILE_URL = "/api/get_mobile";

    // 用户获取短信 URL
    private static final String GET_MESSAGE_URL = "/api/get_message";

    // 用户释放号码 URL
    private static final String FREE_MOBILE_URL = "/api/free_mobile";

    // 用户拉黑号码 URL
    private static final String ADD_BLACKLIST_URL = "/api/add_blacklist";

    @PostConstruct
    public void init() {

        YeZiLoginDto result = login(USERNAME, PASSWORD);
        YEZI_TOKEN = result.getToken();
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public static YeZiLoginDto login(String username, String password) {

        Map<String, String> params = new HashMap<>() {{

            put("username", username);
            put("password", password);
        }};
        YeZiLoginDto result = LmyXlfHttp
                .get(MAIN_DOMAIN + LOGIN_URL)
                .urlParams(params)
                .build()
                .json(YeZiLoginDto.class);

        if (!LOGIN_SUCCESS_MSG.equals(result.getMessage())) {

            log.error("椰子登录失败，账号或密码错误，username:{}，password:{}，result:{}", username, password, result);
            return new YeZiLoginDto();
            // throw new LmyXlfException(result.getMessage());
        }
        log.info("椰子登录成功，username:{}，password:{}，result:{}", username, password, result);
        return result;
    }

    /**
     * 获取手机号
     *
     * @param token     token
     * @param projectId 项目 id；普通项目填普通项目的 id，专属类型也可以填写专属项目的对接码【例：12585----xxxx】
     * @return
     */
    public static YeZiGetMobileDto getMobile(String token, String projectId) {

        return getMobile(token, projectId, null, null, 4, null, null,
                null, null, null, null, null);
    }

    /**
     * 获取手机号
     *
     * @param token        token
     * @param projectId    项目 id；普通项目填普通项目的 id，专属类型也可以填写专属项目的对接码【例：12585----xxxx】
     * @param special      从专属取卡：special=1，不加这个参数取普通项目的卡，注意：此参数只有项目 id 是普通项目的时候才可以
     * @param loop         是否过滤项目，1：过滤，2：不过滤 默认不过滤
     * @param operator     运营商 (0=默认 1=移动 2=联通 3=电信 4=实卡 5=虚卡) 可空
     * @param phoneNum     指定取号的话，这里填要取的手机号
     * @param scope        指定号段，最多支持号码前 5 位. 例如可以为 165，也可以为 16511
     * @param address      归属地选择 例如：湖北，甘肃，不需要带省字
     * @param apiId        如果是开发者,此处填写你的用户 id 才有收益，注意是用户 id 不是登录账号
     * @param scopeBlack   排除号段最长支持 7 位且可以支持多个,最多支持 20 个号段。用逗号分隔 比如 150,1511111,15522
     * @param creatTime    输入整数,单位/天,用来过滤上线时间的机器.比如输入 7,那么获取到的手机号最少上线了 7 天，范围1-60
     * @param designatedID 指定卡商 id
     * @return
     */
    public static YeZiGetMobileDto getMobile(String token, String projectId, Integer special, Integer loop,
                                             Integer operator, String phoneNum, String scope, String address,
                                             String apiId, String scopeBlack, Integer creatTime, String designatedID) {

        Map<String, Object> params = new HashMap<>() {{

            put("token", token);
            put("project_id", projectId);
            put("special", special);
            put("loop", loop);
            put("operator", operator);
            put("phone_num", phoneNum);
            put("scope", scope);
            put("address", address);
            put("api_id", apiId);
            put("scope_black", scopeBlack);
            put("creat_time", creatTime);
            put("designatedID", designatedID);
        }};
        // 除去值为 null 的参数
        Map<String, Object> filteredParams = params.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
        YeZiGetMobileDto result = LmyXlfHttp
                .get(MAIN_DOMAIN + GET_MOBILE_URL)
                .urlParams(filteredParams)
                .build()
                .json(YeZiGetMobileDto.class);

        if (!SUCCESS_MSG.equals(result.getMessage())) {

            log.error("椰子取卡失败，token:{}，projectId:{}，special:{}，loop:{}，operator:{}，phoneNum:{}，" +
                            "scope:{}，address:{}，apiId:{}，scopeBlack:{}，creatTime:{}，designatedID:{}，result:{}",
                    token, projectId, special, loop, operator, phoneNum, scope, address, apiId, scopeBlack, creatTime,
                    designatedID, result);
            throw new LmyXlfException(result.getMessage());
        }
        log.info("椰子取卡成功，手机号:{}，token:{}，projectId:{}，special:{}，loop:{}，operator:{}，phoneNum:{}，" +
                        "scope:{}，address:{}，apiId:{}，scopeBlack:{}，creatTime:{}，designatedID:{}，result:{}",
                result.getMobile(), token, projectId, special, loop, operator, phoneNum, scope, address, apiId,
                scopeBlack, creatTime, designatedID, result);
        return result;
    }

    /**
     * 获取短信
     *
     * @param token     token
     * @param projectId 项目 id；普通项目填普通项目的 id，专属类型也可以填写专属项目的对接码【例：12585----xxxx】
     * @param special   如果取卡时调用了此参数，这里必须要填 special=1，否则获取不到短信
     * @param phoneNum  get_mobile 取卡接口返回的手机号
     * @return
     */
    public static YeZiGetMessageDto getMessage(String token, String projectId, String special, String phoneNum) {

        Map<String, String> params = new HashMap<>() {{

            put("token", token);
            put("project_id", projectId);
            put("special", special);
            put("phone_num", phoneNum);
        }};
        // 除去值为 null 的参数
        Map<String, Object> filteredParams = params.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
        YeZiGetMessageDto result = LmyXlfHttp
                .get(MAIN_DOMAIN + GET_MESSAGE_URL)
                .urlParams(filteredParams)
                .build()
                .json(YeZiGetMessageDto.class);

        if (SUCCESS_MSG.equals(result.getMessage())) {

            log.info("短信已到达，token:{}，projectId:{}，special:{}，phoneNum:{}，result:{}",
                    token, projectId, special, phoneNum, result);
            return result;
        }
        log.info("短信未到达，token:{}，projectId:{}，special:{}，phoneNum:{}，result:{}",
                token, projectId, special, phoneNum, result);
        return null;
    }

    /**
     * 释放号码
     *
     * @param token     token
     * @param projectId 普通项目填普通项目的 id，专属类型也可以填写专属项目的对接码【例：12585----xxxx】
     * @param special   如果取卡时调用了此参数，这里必须要填 special=1，否则不能释放号码
     * @param phoneNum  get_mobile 取卡接口返回的手机号
     * @return
     */
    public static YeZiAddBlacklist freeMobile(String token, String projectId, String special, String phoneNum) {

        Map<String, String> params = new HashMap<>() {{

            put("token", token);
            put("project_id", projectId);
            put("special", special);
            put("phone_num", phoneNum);
        }};
        // 除去值为 null 的参数
        Map<String, Object> filteredParams = params.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
        // 返回格式与拉黑号码返回格式一致
        YeZiAddBlacklist result = LmyXlfHttp
                .get(MAIN_DOMAIN + FREE_MOBILE_URL)
                .urlParams(filteredParams)
                .build()
                .json(YeZiAddBlacklist.class);

        log.info("释放号码，result:{}，token:{}，projectId:{}，special:{}，phoneNum:{}",
                result, token, projectId, special, phoneNum);
        return result;
    }

    /**
     * 拉黑号码
     *
     * @param token     token
     * @param projectId 普通项目填普通项目的 id，专属类型也可以填写专属项目的对接码【例：12585----xxxx】
     * @param special   如果取卡时调用了此参数，这里必须要填 special=1，否则拉黑不了号码
     * @param phoneNum  get_mobile 取卡接口返回的手机号
     * @return
     */
    public static YeZiAddBlacklist addBlacklist(String token, String projectId, String special, String phoneNum) {

        Map<String, String> params = new HashMap<>() {{

            put("token", token);
            put("project_id", projectId);
            put("special", special);
            put("phone_num", phoneNum);
        }};
        // 除去值为 null 的参数
        Map<String, Object> filteredParams = params.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
        YeZiAddBlacklist result = LmyXlfHttp
                .get(MAIN_DOMAIN + ADD_BLACKLIST_URL)
                .urlParams(filteredParams)
                .build()
                .json(YeZiAddBlacklist.class);

        log.info("拉黑号码，result:{}，token:{}，projectId:{}，special:{}，phoneNum:{}",
                result, token, projectId, special, phoneNum);
        return result;
    }
}