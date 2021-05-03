package com.yuman.model;

import lombok.Data;

@Data
public class PageModel<T> {
    private Integer pageIndex;
    private Integer pageSize;
    private Long pageTotal;
    private T data;
}
