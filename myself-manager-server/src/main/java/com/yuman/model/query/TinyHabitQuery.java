package com.yuman.model.query;

import lombok.Data;



@Data
public class TinyHabitQuery {
    private Integer pageIndex;
    private Integer pageSize;
    private Integer state;
    private String taskName;
}
