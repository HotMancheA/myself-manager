package com.yuman.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuman.entity.ForgetCurve;
import com.yuman.entity.TinyHabit;
import com.yuman.entity.TinyHabitLog;
import com.yuman.mapper.TinyHabitLogMapper;
import com.yuman.mapper.TinyHabitMapper;
import com.yuman.model.MemoryTaskModel;
import com.yuman.model.PageModel;
import com.yuman.model.TinyHabitModel;
import com.yuman.model.TinyLogModel;
import com.yuman.model.query.TinyHabitQuery;
import com.yuman.model.response.BaseResponse;
import com.yuman.repository.TinyHabitLogRepository;
import com.yuman.repository.TinyHabitRepository;
import com.yuman.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tinyHabit")
public class TinyHabitController {


    @Autowired
    TinyHabitRepository tinyHabitRepository;

    @Autowired
    TinyHabitLogRepository tinyHabitLogRepository;

    @Resource
    TinyHabitMapper tinyHabitMapper;

    @Resource
    TinyHabitLogMapper tinyHabitLogMapper;

    @PostMapping("/list")
    public BaseResponse<PageModel<List<TinyHabitModel>>> list(@RequestBody TinyHabitQuery tinyHabitQuery) {
        Page<TinyHabit> page = PageHelper.startPage(tinyHabitQuery.getPageIndex(), tinyHabitQuery.getPageSize());
        QueryWrapper<TinyHabit> queryWrapper = new QueryWrapper<>();
        if (tinyHabitQuery.getState() != null) {
            queryWrapper.eq("state", tinyHabitQuery.getState());
        }
        if (StringUtils.isNotEmpty(tinyHabitQuery.getTaskName())) {
            queryWrapper.like("target", tinyHabitQuery.getTaskName());
        }
        tinyHabitMapper.selectList(queryWrapper);
        List<TinyHabitModel> collect = page.getResult().stream().map(o -> convertTinyHabit(o)).collect(Collectors.toList());
        PageModel<List<TinyHabitModel>> listPageModel = PageUtil.convertPage(page, collect);
        return BaseResponse.ok(listPageModel);
    }

    @GetMapping("/listLog")
    public BaseResponse listLog(Integer pageIndex, Integer pageSize, String id) {
        Page<TinyHabitLog> page = PageHelper.startPage(pageIndex, pageSize);
        QueryWrapper<TinyHabitLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tiny_habit_id", id);
        queryWrapper.orderByDesc("start_time");
        tinyHabitLogMapper.selectList(queryWrapper);
        List<TinyHabitLog> collect = page.getResult().stream().map(o -> convertLog(o)).collect(Collectors.toList());
        PageModel<List<TinyHabitLog>> listPageModel = PageUtil.convertPage(page, collect);
        int day = 0;
        if (collect.size() > 0) {
            day = daysBetween(collect.get(collect.size() - 1).getStartTime(), new Date());
        }
        List<TinyHabitLog> punchCardList = collect.stream().filter(o -> convertPunchCard(o)).collect(Collectors.toList());
        TinyLogModel tinyLogModel = new TinyLogModel();
        tinyLogModel.setTotalDay(day);
        tinyLogModel.setPunchCardDay(punchCardList.size());
        tinyLogModel.setData(listPageModel);
        return BaseResponse.ok(tinyLogModel);
    }

    private boolean convertPunchCard(TinyHabitLog o) {
        if(o.getStartTime() == null || o.getEndTime() == null){
            return false;
        }
        return true;
    }

    public static int daysBetween(Date sendDate, Date startDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            sendDate = sdf.parse(sdf.format(sendDate));
            startDate = sdf.parse(sdf.format(startDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(sendDate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(startDate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }


    public TinyHabitLog convertLog(TinyHabitLog tinyHabitLog) {
        if (tinyHabitLog.getPunchCardState() == 0) {
            tinyHabitLog.setPunchCardStateText("未打卡");
        } else if (tinyHabitLog.getPunchCardState() == 1) {
            tinyHabitLog.setPunchCardStateText("打卡中");
        } else if (tinyHabitLog.getPunchCardState() == 2) {
            tinyHabitLog.setPunchCardStateText("打卡完成");
        }
        return tinyHabitLog;
    }


    @GetMapping("/punchCard")
    public BaseResponse<?> punchCard(String id) {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        TinyHabitLog tinyHabitLog = tinyHabitLogRepository.findByStartTime(today, id);
        if (tinyHabitLog == null) {
            tinyHabitLog = new TinyHabitLog();
            tinyHabitLog.setPunchCardState(1);
            tinyHabitLog.setTinyHabitId(id);
            tinyHabitLog.setStartTime(new Date());
        } else {
            tinyHabitLog.setEndTime(new Date());
            tinyHabitLog.setPunchCardState(2);
            tinyHabitLog.setExecuteCount(1);
            tinyHabitLog.setTimeInterval(timeInterval(tinyHabitLog));
            tinyHabitLog.setUnitName("分钟");
        }
        tinyHabitLogRepository.save(tinyHabitLog);
        return BaseResponse.ok();
    }

    @GetMapping("/executeCount")
    public BaseResponse<?> executeCount(String id) {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        TinyHabitLog tinyHabitLog = tinyHabitLogRepository.findByStartTime(today, id);
        tinyHabitLog.setExecuteCount(tinyHabitLog.getExecuteCount() + 1);
        tinyHabitLogRepository.save(tinyHabitLog);
        return BaseResponse.ok();
    }

    private Integer timeInterval(TinyHabitLog tinyHabitLog) {
        Date startTime = tinyHabitLog.getStartTime();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startTime);
        Calendar instance = Calendar.getInstance();
        int startHour = startCalendar.get(Calendar.HOUR);
        int hour = instance.get(Calendar.HOUR);
        int startMinute = startCalendar.get(Calendar.MINUTE);
        int minute = instance.get(Calendar.MINUTE);
        Integer time = (hour - startHour) * 60 + (minute - startMinute);
        return time;
    }


    @PostMapping("/add")
    public BaseResponse<?> add(@RequestBody TinyHabit tinyHabit) {
        tinyHabit.setState(1);
        tinyHabitRepository.save(tinyHabit);
        return BaseResponse.ok();
    }

    @PostMapping("/edit")
    public BaseResponse<?> edit(@RequestBody TinyHabit tinyHabit) {
        tinyHabitRepository.save(tinyHabit);
        return BaseResponse.ok();
    }

    @PostMapping("/del")
    public BaseResponse<?> del(@RequestBody String[] ids) {
        for (String id : ids) {
            tinyHabitRepository.deleteById(id);
            tinyHabitLogRepository.deleteTinyHabitLogByTinyHabitId(id);
        }
        return BaseResponse.ok();
    }


    public TinyHabitModel convertTinyHabit(TinyHabit tinyHabit) {
        TinyHabitModel tinyHabitModel = new TinyHabitModel();
        tinyHabitModel.setId(tinyHabit.getId());
        tinyHabitModel.setDescription(tinyHabit.getDescription());
        tinyHabitModel.setTaskName(tinyHabit.getTaskName());
        tinyHabitModel.setState(tinyHabit.getState());
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        TinyHabitLog tinyHabitLog = tinyHabitLogRepository.findByStartTime(today, tinyHabit.getId());
        tinyHabitModel.setPunchCardState(0);
        tinyHabitModel.setPunchCardStateText("未打卡");
        if (tinyHabitLog != null) {
            tinyHabitModel.setPunchCardState(1);
            tinyHabitModel.setPunchCardStateText("打卡中");
        } else {
            return tinyHabitModel;
        }
        Integer endTime = tinyHabitLogRepository.findByEndTime(today, tinyHabit.getId());
        if (endTime != 0) {
            tinyHabitModel.setPunchCardState(2);
            tinyHabitModel.setPunchCardStateText("打卡完成");
        }
        return tinyHabitModel;
    }
}
