package com.lmyxlf.jian_mu.global.exception;

import com.lmyxlf.jian_mu.global.exception.handler.CommonExceptionHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description 实现该异常的同步必须要实现对应的异常处理器
 *
 *              {@link CommonExceptionHandler#abstractLmyXlfExceptionHandler(AbstractLmyXlfException)}
 *              @ExceptionHandler({AbstractLmyXlfException.class})
 *              public LmyXlfResult<Object> abstractLmyXlfExceptionHandler(AbstractLmyXlfException e) {
 *                  // 判断异常信息是否是错误码类型
 *                  LmyXlfResult<Object> result;
 *                  Object error = e.getError();
 *                  log.debug("abstractLmyXlfExceptionHandler, error:{}, message:{}", error, e.getMessage());
 *                  if (error instanceof CodeMsg) {
 *                      result = LmyXlfResult.error((CodeMsg) error, e.getMessage());
 *                  } else {
 *                      result = LmyXlfResult.error(error);
 *                  }
 *                  return result;
 *              }
 * @since 17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractLmyXlfException extends RuntimeException{
    private Object error;

    public AbstractLmyXlfException() {
    }

    public AbstractLmyXlfException(Object error) {
        this.error = error;
    }

    public AbstractLmyXlfException(Object error, String msg) {
        super(msg);
        this.error = error;
    }

}