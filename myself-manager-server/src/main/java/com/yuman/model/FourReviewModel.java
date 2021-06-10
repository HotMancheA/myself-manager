package com.yuman.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FourReviewModel {

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
