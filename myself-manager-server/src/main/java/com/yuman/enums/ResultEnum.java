package com.yuman.enums;

public enum ResultEnum {

    SUCCESS(200,"成功"),
    ERROR(500,"失败");

    private Integer code;

    private String message;

    private ResultEnum(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }

}
