package com.yuman.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
public class ForgetCurveItemResult {


    private String id;


    private String targetId;

    private Integer cycle;

    @Column(name = "date_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date dateTime;


    private Integer status;

    private String status_text;

}
