package com.yuman.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class MemoryTaskModel {


    private String id;

    private String target;

    private String description;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date nextReviewTime;

    private Integer state;

    private String stateText;

    private Integer status;

    private Integer orderNum;

}
