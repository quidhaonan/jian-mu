package com.lmyxlf.jian_mu.global.constant;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description 返回结果状态码定义
 * @since 17
 */
public enum CODE_MSG implements CodeMsg {
    /**
     * 请求成功
     */
    SUCCESS("200", "请求成功"),

    /**
     * 请求成功
     */
    SUCCESS_NO_RESULT("lmyxlf2001", "查询成功，无结果"),

    /**
     * 未授权、未登录（或授权信息错误）
     */
    UNAUTHORIZED("401", "未授权、未登录（或授权信息错误）"),

    /**
     * 访问的资源没有权限（访问被拒绝）
     */
    PERMISSION_DENIED("403", "访问的资源没有权限（访问被拒绝）"),

    /**
     * 该账号已在其他地方登录
     */
    LOG_IN_ELSEWHERE("403","该账号已在其他地方登录"),

    /**
     * 请求url不存在
     */
    NOT_FOUND("404", "请求 url 不存在"),

    /**
     * 请求失败
     */
    ERROR("500", "请求失败"),

    /**
     * 验证码错误
     */
    VERIFY_CODE_ERROR("lmyxlf4001", "验证码错误"),
    /**
     * 该手机号码已注册
     */
    ALREADY_REGISTERED("lmyxlf4002", "该手机号码已注册"),

    /**
     * 该邮箱已注册
     */
    MAILBOX_ALREADY_REGISTERED("lmyxlf4003", "该邮箱已注册"),

    /**
     * 该邮箱不存在
     */
    MAILBOX_NOT_EXIST("lmyxlf4004", "该邮箱不存在"),

    /**
     * 参数校验错误
     */
    ARGUMENT_NOT_VALID("lmyxlf4100", "参数校验错误"),

    /**
     * 参数解析错误
     */
    ARGUMENT_PARSE_FAIL("lmyxlf4101", "参数格式错误"),

    /**
     * 用户名密码错误
     */
    USERNAME_PASSWORD_INVALID("lmyxlf4102", "登陆失败或该账户已经被登录"),

    /**
     * 密码错误（用户修改密码等情况）
     */
    PASSWORD_INVALID("lmyxlf5106", "密码错误"),

    /**
     * appId 不存在
     */
    NOT_APP_ID("lmyxlf4200", "appId 不存在"),

    /**
     * 请求次数超出限制
     */
    REQUEST_COUNT_LIMIT("lmyxlf4201", "请求次数超出限制"),

    /**
     * 车机请求次数超出限制
     */
    REQUEST_COUNT_LIMIT_UID("lmyxlf4202", "当前车机请求次数超出限制"),

    /**
     * 当前项目请求次数超出限制
     */
    REQUEST_COUNT_LIMIT_APP_ID("lmyxlf4203", "当前项目请求次数超出限制"),

    /**
     * 已达最大并发
     */
    REQUEST_CONCURRENT_LIMIT("lmyxlf4204", "已达当前最大并发，稍后重试"),

    /**
     * 没有签名
     */
    NO_SIGN("lmyxlf4300", "没有签名"),

    /**
     * 签名错误
     */
    SIGN_FAIL("lmyxlf4301", "签名错误"),

    /**
     * 系统内部错误
     */
    SYSTEM_ERROR("lmyxlf5000", "系统内部错误"),


    /**
     * 最大文件上传限制异常
     */
    MAXIMUM_UPLOAD_SIZE_EXCEEDED("lmyxlf5001", "上传文件大小超出限制"),


    /**
     * 有效期到期
     */
    EXPIRED("lmyxlf5100", "有效期到期"),

    /**
     * 服务关闭
     */
    SERVICE_CLOSED("lmyxlf5101", "服务关闭"),

    /**
     * 服务下架
     */
    SERVICE_OFF_THE_SHELF("lmyxlf5102", "服务下架"),

    /**
     * token过期
     */
    TOKEN_EXPIRED("lmyxlf5103", "token 过期"),

    /**
     * 内部服务调用出错
     */
    SERVICE_UNAVAILABLE("lmyxlf5104", "服务繁忙，请稍后再试"),

    /**
     * 重复主键或者重复key
     */
    DUPLICATE_KEY("lmyxlf5105", "数据重复"),

    /**
     * 仅用在union_account 无权限
     */
    MISS_SUPER_PERMISSION("lmyxlf5106","高危操作需要最高权限，请通知管理员"),

    /**
     * 仅在union_account 存在用户无角色
     */
    MISS_ROLE("lmyxlf5107","请通知管理员给你开通权限"),

    /**
     * 第三方服务异常
     */
    OTHER_SERVICE_UNAVAILABLE("lmyxlf5108","第三方服务异常");

    String code;
    String msg;

    CODE_MSG(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public static CODE_MSG getCode(String code) {
        for (CODE_MSG value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}