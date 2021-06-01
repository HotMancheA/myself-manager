package com.yuman.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuman.entity.LearnTime;
import com.yuman.entity.TinyHabit;
import com.yuman.entity.TinyHabitLog;
import com.yuman.mapper.LearnTimeMapper;
import com.yuman.mapper.TinyHabitLogMapper;
import com.yuman.mapper.TinyHabitMapper;
import com.yuman.model.PageModel;
import com.yuman.model.TinyHabitModel;
import com.yuman.model.TinyLogModel;
import com.yuman.model.query.LearnTimeQuery;
import com.yuman.model.query.TinyHabitQuery;
import com.yuman.model.response.BaseResponse;
import com.yuman.repository.LearnTimeRepository;
import com.yuman.repository.TinyHabitLogRepository;
import com.yuman.repository.TinyHabitRepository;
import com.yuman.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/learnTime")
public class LearnTimeController {


    @Autowired
    LearnTimeRepository learnTimeRepository;


    @Resource
    LearnTimeMapper learnTimeMapper;

    @PostMapping("/list")
    public BaseResponse<PageModel<List<TinyHabitModel>>> list(@RequestBody LearnTimeQuery learnTimeQuery) {
        Page<LearnTime> page = PageHelper.startPage(learnTimeQuery.getPageIndex(), learnTimeQuery.getPageSize());
        QueryWrapper<LearnTime> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(learnTimeQuery.getLearnName())) {
            queryWrapper.like("learn_name", learnTimeQuery.getLearnName());
        }
        learnTimeMapper.selectList(queryWrapper);
        List<LearnTime> collect = page.getResult();
        PageModel<List<LearnTime>> listPageModel = PageUtil.convertPage(page, collect);
        return BaseResponse.ok(listPageModel);
    }



    @PostMapping("/add")
    public BaseResponse<?> add(@RequestBody LearnTime learnTime) {
        learnTimeRepository.save(learnTime);
        return BaseResponse.ok();
    }

    @PostMapping("/edit")
    public BaseResponse<?> edit(@RequestBody LearnTime learnTime) {
        learnTimeRepository.save(learnTime);
        return BaseResponse.ok();
    }

    @PostMapping("/del")
    public BaseResponse<?> del(@RequestBody String[] ids) {
        for (String id : ids) {
            learnTimeRepository.deleteById(id);
        }
        return BaseResponse.ok();
    }


}
