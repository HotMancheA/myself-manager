package com.yuman.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yuman.enums.ResultEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口返回数据格式
 */
@Data
@ApiModel(value = "接口返回对象", description = "接口返回对象")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    @ApiModelProperty(value = "成功标志")
    private boolean success = true;

    /**
     * 返回处理消息
     */
    @ApiModelProperty(value = "返回处理消息")
    private String message = "操作成功！";

    /**
     * 返回代码
     */
    @ApiModelProperty(value = "返回代码")
    private Integer code = 0;

    /**
     * 返回数据对象 data
     */
    @ApiModelProperty(value = "返回数据对象")
    private T data;

    /**
     * 时间戳
     */
    @ApiModelProperty(value = "时间戳")
    private long timestamp = System.currentTimeMillis();


    public Result(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }


    public static Result ok() {
        Result result = new Result(ResultEnum.SUCCESS);
        return result;
    }


    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>(ResultEnum.SUCCESS);
        result.setData(data);
        return result;
    }



    public static <T> Result<T> error(ResultEnum resultEnum) {
        Result<T> result = new Result<T>(resultEnum);
        result.setSuccess(false);
        return result;
    }



}