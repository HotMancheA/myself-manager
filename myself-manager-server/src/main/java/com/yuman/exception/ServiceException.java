package com.yuman.exception;

public class ServiceException extends RuntimeException{


    //异常信息
    public String message;

    public ServiceException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
