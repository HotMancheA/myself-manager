package com.yuman.model.query;

import lombok.Data;


@Data
public class LearnTimeQuery {
    private Integer pageIndex;
    private Integer pageSize;
    private String learnName;
}
