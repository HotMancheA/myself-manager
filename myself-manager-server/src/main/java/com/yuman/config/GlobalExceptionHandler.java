package com.yuman.config;

import com.yuman.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = ServiceException.class)
    public String badRequestException(ServiceException e) {
        return e.getMessage();
    }


    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return "could_not_read_json";
    }


    /**
     * 处理所有不可知的异常
     *
     * @param e
     */
    @ExceptionHandler(Exception.class)
    public String handlerException(Exception e) {
        String stackTrace = e.getStackTrace().length > 0 ? e.getStackTrace()[0].toString() : "";
        String errorMsg = "系统未捕获的异常handlerException：error:" + e.toString() + "\n" + "stackTrace:" + stackTrace;
        log.error(errorMsg);
        return errorMsg;
    }


}
