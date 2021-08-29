package com.yuman.utils;

import com.github.pagehelper.Page;
import com.yuman.model.PageModel;

import java.util.List;

public class PageUtil {


    public static <O, I> PageModel<O> convertPage(Page<I> resultPage, O content) {
        PageModel<O> pageModel = new PageModel<>();
        pageModel.setPageIndex(resultPage.getPageNum());
        pageModel.setPageTotal(resultPage.getTotal());
        pageModel.setPageSize(resultPage.getPageSize());
        pageModel.setData(content);
        return pageModel;
    }


}
