package com.yuman.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuman.entity.ForgetCurveItem;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Data
public class ForgetCurveModel {

    private String targetId;

    private String target;

    private String description;

    private String forgetCurveItemId;

    private Integer cycle;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date dateTime;

    private Integer status;

    private String status_text;

}
