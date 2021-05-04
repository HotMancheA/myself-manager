package com.yuman.model;

import lombok.Data;

import javax.persistence.Column;

@Data
public class TinyHabitModel {

    private String id;

    private String taskName;

    private String description;

    //任务状态，0 关闭，1 开启
    private Integer state;


    private Integer punchCardState;


    private String punchCardStateText;


}
