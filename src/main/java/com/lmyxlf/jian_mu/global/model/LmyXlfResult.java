package com.lmyxlf.jian_mu.global.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lmyxlf.jian_mu.global.constant.CODE_MSG;
import com.lmyxlf.jian_mu.global.constant.CodeMsg;
import com.lmyxlf.jian_mu.global.constant.TraceConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.MDC;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description 统一返回结果
 * @since 17
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "统一返回结果")
public class LmyXlfResult<T> {
    /**
     * 错误码
     */
    @ApiModelProperty(name = "code", value = "错误码", required = true, position = 1)
    private String code;

    /**
     * 错误信息
     */
    @ApiModelProperty(name = "msg", value = "错误信息", required = true, position = 2)
    private String msg;

    /**
     * 链路调用 id
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(name = "traceId", value = "链路调用 id", required = true, position = 3)
    private String traceId;

    /**
     * 数据来源
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(name = "origin", value = "数据来源，当调用聚合服务时会有返回，其它能明确数据来源的不返回值", position = 4)
    private String origin;

    /**
     * 内容
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(name = "data", value = "数据内容", position = 5)
    private T data;

    public LmyXlfResult() {
    }

    private LmyXlfResult(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.traceId = MDC.get(TraceConstant.TRACE_ID);
        this.data = data;
    }

    public static <T> LmyXlfResult<T> ok(String code, String msg, T date) {
        return new LmyXlfResult<>(code, msg, date);
    }

    public static <T> LmyXlfResult<T> ok(String code, String msg) {
        return ok(code, msg, null);
    }

    public static <T> LmyXlfResult<T> ok() {
        return ok(CODE_MSG.SUCCESS.getCode(), CODE_MSG.SUCCESS.getMsg());
    }

    public static <T> LmyXlfResult<T> ok(T data) {
        return ok(CODE_MSG.SUCCESS.getCode(), CODE_MSG.SUCCESS.getMsg(), data);
    }

    public static <T> LmyXlfResult<PageData<T>> ok(IPage<T> iPage) {
        return ok(CODE_MSG.SUCCESS.getCode(), CODE_MSG.SUCCESS.getMsg(), new PageData<>(iPage));
    }

    public static <T> LmyXlfResult<T> error(String code, String msg) {
        return ok(code, msg);
    }

    public static <T> LmyXlfResult<T> error() {
        return ok(CODE_MSG.ERROR.getCode(), CODE_MSG.ERROR.getMsg());
    }

    public static <T> LmyXlfResult<T> error(String msg) {
        return error(CODE_MSG.ERROR.getCode(), msg);
    }

    public static <T> LmyXlfResult<T> error(T object) {
        return error(CODE_MSG.ERROR, object);
    }

    public static <T> LmyXlfResult<T> error(CodeMsg codeMsg) {
        return ok(String.valueOf(codeMsg.getCode()), codeMsg.getMsg());
    }

    public static <T> LmyXlfResult<T> error(CodeMsg codeMsg, T data) {
        return ok(String.valueOf(codeMsg.getCode()), codeMsg.getMsg() + (data == null ? "" : (": " + data.toString())), null);
    }

    public static <T> LmyXlfResult<T> auto(boolean b, T data) {
        if (b) {
            return ok(data);
        } else {
            return error(data);
        }
    }

    public static LmyXlfResult<Boolean> auto(boolean b, String msg) {
        if (b) {
            return ok(true);
        } else {
            return ok(CODE_MSG.ERROR.getCode(), msg, false);
        }
    }
}