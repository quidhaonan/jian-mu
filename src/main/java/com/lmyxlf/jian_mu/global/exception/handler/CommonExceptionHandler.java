package com.lmyxlf.jian_mu.global.exception.handler;

import com.lmyxlf.jian_mu.global.constant.CODE_MSG;
import com.lmyxlf.jian_mu.global.constant.CodeMsg;
import com.lmyxlf.jian_mu.global.exception.AbstractLmyXlfException;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import com.lmyxlf.jian_mu.global.exception.MyException;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description 自定义异常处理类
 *              feignUnavailable 方法大部分监控报点，直接注释，新增打印语句
 * @since 17
 */
@RestControllerAdvice
@Slf4j
@ResponseStatus(HttpStatus.OK)
public class CommonExceptionHandler {

    /**
     * 处理抛出的自定义异常
     *
     * @param e MyException
     * @return LmyXlfResult
     */
    @ExceptionHandler({MyException.class, LmyXlfException.class})
    public LmyXlfResult<Object> myExceptionHandler(RuntimeException e) {
        // 1、获取异常信息
        Object error = null;
        if (e instanceof LmyXlfException) {
            error = ((LmyXlfException) e).getError();
        } else if (e instanceof MyException) {
            error = ((MyException) e).getError();
        } else {
            error = "";
        }
        // 判断异常信息是否是错误码类型
        LmyXlfResult<Object> result;
        if (error instanceof CodeMsg) {
            result = LmyXlfResult.error((CodeMsg) error, e.getMessage());
        } else {
            result = LmyXlfResult.error(error);
        }
        // 由于这里处理的是自定义异常，异常日志应该由抛出方打印，此处不再重复打印
        reportMonitor();
        return result;
    }

    /**
     * 处理抛出的自定义异常
     *
     * @param e AbstractLmyXlfException
     * @return LmyXlfResult
     */
    @ExceptionHandler({AbstractLmyXlfException.class})
    public LmyXlfResult<Object> abstractLmyXlfExceptionHandler(AbstractLmyXlfException e) {
        reportMonitor();
        LmyXlfResult<Object> result;
        Object error = e.getError();
        log.debug("abstractLmyXlfExceptionHandler, error:{}, message:{}", error, e.getMessage());
        // 判断异常信息是否是错误码类型
        if (error instanceof CodeMsg) {
            result = LmyXlfResult.error((CodeMsg) error, e.getMessage());
        } else {
            result = LmyXlfResult.error(error);
        }
        return result;
    }

    /**
     * 处理参数校验异常
     *
     * @param e MethodArgumentNotValidException
     * @return LmyXlfResult
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public LmyXlfResult<String> validExceptionHandler(MethodArgumentNotValidException e) {
        reportMonitor();
        return parseBindException(e.getBindingResult());
    }

    /**
     * 处理参数绑定异常
     *
     * @param e BindException
     * @return LmyXlfResult
     */
    @ExceptionHandler(BindException.class)
    public LmyXlfResult<String> validExceptionHandler(BindException e) {
        reportMonitor();
        if (e.hasFieldErrors()) {
            return parseBindException(e.getBindingResult());
        }
        return LmyXlfResult.error(CODE_MSG.ARGUMENT_NOT_VALID);

    }

    /**
     * 处理参数绑定异常
     *
     * @param e BindException
     * @return LmyXlfResult
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    public LmyXlfResult<String> validExceptionHandler(MissingRequestHeaderException e) {
        String message = e.getMessage();
        // Monitor.warn("miss_request_header").log("缺少请求头参数：{}", Objects.requireNonNullElseGet(message, e::getHeaderName)).inc();
        log.warn("缺少请求头参数：{}", Objects.requireNonNullElseGet(message, e::getHeaderName));
        reportMonitor();
        return LmyXlfResult.error(CODE_MSG.ARGUMENT_NOT_VALID);
    }

    private LmyXlfResult<String> parseBindException(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            String defaultMessage = fieldError.getDefaultMessage();
            String field = fieldError.getField();
            sb.append(field).append(":").append(defaultMessage).append(";");
        }
        // Monitor.warn("param_binding_error").log(sb.toString()).inc();
        log.warn(sb.toString());
        return LmyXlfResult.error(CODE_MSG.ARGUMENT_NOT_VALID);
    }

    /**
     * 处理参数解析异常
     *
     * @param e HttpMessageNotReadableException
     * @return LmyXlfResult
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public LmyXlfResult<Object> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        // Monitor.warn("param_parsing_exception").log(e.getMessage()).inc();
        log.warn(e.getMessage());
        reportMonitor();
        return LmyXlfResult.error(CODE_MSG.ARGUMENT_PARSE_FAIL);
    }

    /**
     * 包装类参数解析异常
     *
     * @param e ConstraintViolationException
     * @return LmyXlfResult
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public LmyXlfResult<String> handler(ConstraintViolationException e) {
        StringBuilder builder = new StringBuilder();
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            String path = constraintViolation.getPropertyPath().toString();
            builder.append(path.substring(path.lastIndexOf(".") + 1))
                    .append(":")
                    .append(constraintViolation.getMessage())
                    .append(";");
        }
        String errorMsg = builder.toString();
        // Monitor.warn("param_binding_error").log(errorMsg).inc();
        log.warn(errorMsg);
        reportMonitor();
        return LmyXlfResult.error(CODE_MSG.ARGUMENT_NOT_VALID);
    }


    /**
     * 处理 400 异常
     *
     * @param e NoHandlerFoundException
     * @return LmyXlfResult
     */
    @ExceptionHandler(TypeMismatchException.class)
    public LmyXlfResult<Object> typeMismatchException(TypeMismatchException e) {
        // Monitor.warn("http_400").log(e.getMessage()).inc();
        log.warn(e.getMessage());
        reportMonitor();
        return LmyXlfResult.error(CODE_MSG.ARGUMENT_PARSE_FAIL);
    }

    /**
     * 处理 404 异常
     *
     * @param e NoHandlerFoundException
     * @return LmyXlfResult
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public LmyXlfResult<Object> noHandlerFoundException(NoHandlerFoundException e) {
        // Monitor.warn("http_404").log("path: {}, method: {} not found", e.getRequestURL(), e.getHttpMethod()).inc();
        log.warn("path: {}, method: {} not found", e.getRequestURL(), e.getHttpMethod());
        reportMonitor();
        return LmyXlfResult.error(CODE_MSG.NOT_FOUND);
    }

    /**
     * 处理 请求方法不支持 异常
     *
     * @param e HttpRequestMethodNotSupportedException
     * @return LmyXlfResult
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public LmyXlfResult<Object> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        // Monitor.warn("http_method_no_support").tag("method", e.getMethod()).inc();
        log.warn(e.getMessage());
        reportMonitor();
        return LmyXlfResult.error(CODE_MSG.NOT_FOUND);
    }

    /**
     * 处理 最大文件上传限制异常
     *
     * @param e MaxUploadSizeExceededException
     * @return LmyXlfResult
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public LmyXlfResult<Object> maxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        // Monitor.warn("max_upload_size_exceeded").log(e.getMessage()).inc();
        log.warn(e.getMessage());
        reportMonitor();
        return LmyXlfResult.error(CODE_MSG.MAXIMUM_UPLOAD_SIZE_EXCEEDED);
    }

    /**
     * sql异常
     *
     * @param e SQLException
     * @return LmyXlfResult
     */
    @ExceptionHandler(SQLException.class)
    public LmyXlfResult<Object> sql(SQLException e) {
        // Monitor.error("sql_error").unLog();
        log.error("sql异常：", e);
        reportMonitor();
        return LmyXlfResult.error();
    }

    /**
     * sql异常
     *
     * @param e SQLException
     * @return LmyXlfResult
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public LmyXlfResult<Object> duplicateKeyException(DuplicateKeyException e, HttpServletRequest httpServletRequest) {
        // 数据库重复key
        // Monitor.warn("sql_duplicate_key").unLog();
        log.warn("数据库主键重复：", e);
        log.warn("请求的url:{}", httpServletRequest.getRequestURI());
        reportMonitor();
        return LmyXlfResult.error(CODE_MSG.DUPLICATE_KEY);
    }

    @ExceptionHandler(FeignException.class)
    public LmyXlfResult<Object> feignUnavailable(FeignException e) {
        // Tags tags = Tags.of("status", String.valueOf(e.status()));
        // if (e.hasRequest()) {
        //     Request request = e.request();
        //     URI uri = URI.create(request.url());
        //     tags = tags.and("host", uri.getHost()).and("path", uri.getPath()).and("status", uri.getPath());
        // }
        // Monitor.error("feign_req_error")
        //         .tags(tags)
        //         .log("内部服务调用出现异常:{}, {}", e.contentUTF8(), e.getMessage())
        //         .inc();
        // reportMonitor();
        log.error("内部服务调用出现异常:{}, {}", e.contentUTF8(), e.getMessage());
        return LmyXlfResult.error(CODE_MSG.SERVICE_UNAVAILABLE);
    }

    /**
     * 其它异常
     *
     * @param e Exception
     * @return LmyXlfResult
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public LmyXlfResult<Object> exception(Exception e) {
        log.error(e.getMessage(), e);
        // Monitor.error("unknown_error").unLog().inc();
        reportMonitor();
        return LmyXlfResult.error();
    }

    private void reportMonitor() {
        // Monitor.error("service_exception").unLog().inc();
    }

}