package com.yuman.model;

import lombok.Data;

@Data
public class TinyLogModel {
    private Integer pageIndex;
    private Integer pageSize;
    private Long pageTotal;
    private Integer totalDay;
    private Integer punchCardDay;
    private PageModel data;

}
