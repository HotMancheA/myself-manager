package com.yuman.model.response;

import lombok.Data;

@Data
public class BaseResponse<T> {

    private Integer code;

    private String msg;

    private T data;

    public static <T> BaseResponse ok(T data){
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setCode(200);
        baseResponse.setMsg("成功");
        baseResponse.setData(data);
        return baseResponse;
    }

    public static <T> BaseResponse ok(){
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setCode(200);
        baseResponse.setMsg("成功");
        return baseResponse;
    }

    public static <T> BaseResponse error(String msg){
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setCode(500);
        baseResponse.setMsg(msg);
        return baseResponse;
    }
}
