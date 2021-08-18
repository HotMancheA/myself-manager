package com.yuman.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuman.entity.ForgetCurve;
import com.yuman.entity.ForgetCurveItem;
import com.yuman.entity.FourReview;
import com.yuman.model.ForgetCurveModel;
import com.yuman.model.FourReviewModel;
import com.yuman.model.MemoryTaskModel;
import com.yuman.model.PageModel;
import com.yuman.model.query.ForgetQuery;
import com.yuman.model.response.BaseResponse;
import com.yuman.repository.ForgetCurveItemRepository;
import com.yuman.repository.ForgetCurveRepository;
import com.yuman.repository.FourReviewRepository;
import com.yuman.service.ForgetCurveService;
import com.yuman.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/forgetCurve")
public class ForgetCurveController {

    @Autowired
    private ForgetCurveRepository forgetCurveRepository;

    @Autowired
    private ForgetCurveItemRepository forgetCurveItemRepository;

    @Autowired
    private ForgetCurveService forgetCurveService;

    @Autowired
    private FourReviewRepository fourReviewRepository;

    /**
     * 生成记忆任务
     *
     * @param forgetCurve
     * @return
     */
    @PostMapping("/generateTarget")
    @Transactional
    public BaseResponse generateTarget(@RequestBody ForgetCurve forgetCurve) {
        ForgetCurve save = forgetCurveRepository.save(forgetCurve);
        generateForgetCurveItem(save.getId());
        generateFourReview(save.getId());
        return BaseResponse.ok();
    }

    @PostMapping("/edit")
    public BaseResponse edit(@RequestBody ForgetCurve forgetCurve) {
        ForgetCurve save = forgetCurveRepository.save(forgetCurve);
        return BaseResponse.ok();
    }

    @PostMapping("/del")
    public BaseResponse del(@RequestBody String[] ids) {
        for (String id : ids) {
            forgetCurveRepository.deleteById(id);
            forgetCurveItemRepository.deleteForgetCurveItemByTargetId(id);
            fourReviewRepository.deleteFourReviewByTargetId(id);
        }
        return BaseResponse.ok();
    }

    @GetMapping("/finish")
    public String finish(String id) {
        int finish = forgetCurveItemRepository.finish(id, 1);
        Optional<ForgetCurveItem> optional = forgetCurveItemRepository.findById(id);
        Date nextReviewTime = forgetCurveItemRepository.findNextReviewTime(optional.get().getTargetId());
        if (nextReviewTime == null) {
            forgetCurveRepository.updateStatus(optional.get().getTargetId(), 1);
        }
        return "成功";
    }

    @GetMapping("/fourReviewFinish")
    public String fourReviewFinish(String id) {
        int finish = fourReviewRepository.finish(id, 1);
        Optional<FourReview> optional = fourReviewRepository.findById(id);
        Date nextReviewTime = fourReviewRepository.findNextReviewTime(optional.get().getTargetId());
        if (nextReviewTime == null) {
            forgetCurveRepository.updateStatus(optional.get().getTargetId(), 1);
        }
        return "成功";
    }

    @PostMapping("/list")
    public BaseResponse<PageModel<List<MemoryTaskModel>>> list(@RequestBody ForgetQuery forgetQuery) {
        Page<ForgetCurve> page = PageHelper.startPage(forgetQuery.getPageIndex(), forgetQuery.getPageSize());
        QueryWrapper<ForgetCurve> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("order_num");
        if(forgetQuery.getState() != null){
            queryWrapper.eq("state",forgetQuery.getState());
        }
        if (StringUtils.isNotEmpty(forgetQuery.getTarget())) {
            queryWrapper.like("target",forgetQuery.getTarget());
        }
        forgetCurveService.list(queryWrapper);
        List<MemoryTaskModel> collect = page.getResult().stream().map(o -> convertMemoryTask(o)).collect(Collectors.toList());
        PageModel<List<MemoryTaskModel>> listPageModel = PageUtil.convertPage(page, collect);
        return BaseResponse.ok(listPageModel);
    }

    @GetMapping("/listItem")
    public List<ForgetCurveModel> listItem(String id) {
        Optional<ForgetCurve> optional = forgetCurveRepository.findById(id);
        if (!optional.isPresent()) {
            return new ArrayList<>();
        }
        ForgetCurveItem forgetCurveItem = new ForgetCurveItem();
        forgetCurveItem.setTargetId(optional.get().getId());
        //创建实例
        Example<ForgetCurveItem> example = Example.of(forgetCurveItem);
        List<ForgetCurveItem> forgetCurveItemList = forgetCurveItemRepository.findAll(example);
        List<ForgetCurveModel> forgetCurveModels = forgetCurveItemList.stream().map(o -> convert(o, optional.get())).collect(Collectors.toList());
        return forgetCurveModels;
    }

    @GetMapping("/fourReview")
    public List<FourReviewModel> fourReview(String id) {
        Optional<ForgetCurve> optional = forgetCurveRepository.findById(id);
        if (!optional.isPresent()) {
            return new ArrayList<>();
        }
        FourReview fourReview = new FourReview();
        fourReview.setTargetId(optional.get().getId());
        //创建实例
        Example<FourReview> example = Example.of(fourReview);
        List<FourReview> fourReviewList = fourReviewRepository.findAll(example);

        List<FourReviewModel> fourReviewModels = fourReviewList.stream().map(o -> convert(o, optional.get())).collect(Collectors.toList());
        return fourReviewModels;
    }

    public FourReviewModel convert(FourReview fourReview, ForgetCurve forgetCurve) {
        FourReviewModel fourReviewModel = new FourReviewModel();
        fourReviewModel.setTarget(forgetCurve.getTarget());
        fourReviewModel.setDescription(forgetCurve.getDescription());
        fourReviewModel.setTargetId(fourReview.getTargetId());
        fourReviewModel.setForgetCurveItemId(fourReview.getId());
        fourReviewModel.setCycle(fourReview.getCycle());
        fourReviewModel.setDateTime(fourReview.getDateTime());
        fourReviewModel.setStatus(fourReview.getStatus());
        switch (fourReview.getStatus()) {
            case 0:
                fourReviewModel.setStatus_text("未完成");
                break;
            case 1:
                fourReviewModel.setStatus_text("已完成");
                break;
            case 2:
                fourReviewModel.setStatus_text("已过期");
                break;
            default:
        }
        return fourReviewModel;
    }

    public MemoryTaskModel convertMemoryTask(ForgetCurve forgetCurve) {
        MemoryTaskModel memoryTaskModel = new MemoryTaskModel();
        memoryTaskModel.setId(forgetCurve.getId());
        memoryTaskModel.setDescription(forgetCurve.getDescription());
        memoryTaskModel.setTarget(forgetCurve.getTarget());
        Date nextReviewTime = forgetCurveItemRepository.findNextReviewTime(forgetCurve.getId());
        memoryTaskModel.setStatus(0);
        if (nextReviewTime != null) {
            Calendar instance = Calendar.getInstance();
            int today = instance.get(Calendar.DAY_OF_MONTH);
            instance.setTime(nextReviewTime);
            int day = instance.get(Calendar.DAY_OF_MONTH);
            if (today == day) {
                memoryTaskModel.setStatus(1);
            }
        }
        memoryTaskModel.setNextReviewTime(nextReviewTime);
        Integer state = forgetCurve.getState();
        memoryTaskModel.setState(state);
        if (state == 0) {
            memoryTaskModel.setStateText("进行中");
        } else {
            memoryTaskModel.setStateText("已完成");
        }
        memoryTaskModel.setOrderNum(forgetCurve.getOrderNum());
        return memoryTaskModel;
    }


    public ForgetCurveModel convert(ForgetCurveItem forgetCurveItem, ForgetCurve forgetCurve) {
        ForgetCurveModel forgetCurveModel = new ForgetCurveModel();
        forgetCurveModel.setTarget(forgetCurve.getTarget());
        forgetCurveModel.setDescription(forgetCurve.getDescription());
        forgetCurveModel.setTargetId(forgetCurveItem.getTargetId());
        forgetCurveModel.setForgetCurveItemId(forgetCurveItem.getId());
        forgetCurveModel.setCycle(forgetCurveItem.getCycle());
        forgetCurveModel.setDateTime(forgetCurveItem.getDateTime());
        forgetCurveModel.setStatus(forgetCurveItem.getStatus());
        switch (forgetCurveItem.getStatus()) {
            case 0:
                forgetCurveModel.setStatus_text("未完成");
                break;
            case 1:
                forgetCurveModel.setStatus_text("已完成");
                break;
            case 2:
                forgetCurveModel.setStatus_text("已过期");
                break;
            default:

        }
        return forgetCurveModel;
    }


    private void generateForgetCurveItem(String id) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, 30);
        cover(1, id, instance.getTime());
        instance.add(Calendar.MINUTE, 30);
        instance.add(Calendar.HOUR, 11);
        cover(2, id, instance.getTime());
        instance.add(Calendar.HOUR, 12);
        cover(3, id, instance.getTime());
        instance.add(Calendar.DAY_OF_MONTH, 1);
        cover(4, id, instance.getTime());
        instance.add(Calendar.DAY_OF_MONTH, 2);
        cover(5, id, instance.getTime());
        instance.add(Calendar.DAY_OF_MONTH, 3);
        cover(6, id, instance.getTime());
        instance.add(Calendar.DAY_OF_MONTH, 8);
        cover(7, id, instance.getTime());
    }

    private void generateFourReview(String id) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.HOUR, 24);
        coverFourReview(1, id, instance.getTime());
        instance.add(Calendar.DAY_OF_MONTH, 7);
        coverFourReview(2, id, instance.getTime());
        instance.add(Calendar.DAY_OF_MONTH, 14);
        coverFourReview(3, id, instance.getTime());
        instance.add(Calendar.MONTH, 1);
        coverFourReview(4, id, instance.getTime());
    }

    private void coverFourReview(int cycle, String id, Date time) {
        FourReview fourReview = new FourReview();
        fourReview.setCycle(cycle);
        fourReview.setTargetId(id);
        fourReview.setDateTime(time);
        fourReview.setStatus(0);
        FourReview save = fourReviewRepository.save(fourReview);
    }



    private void cover(int cycle, String id, Date time) {
        ForgetCurveItem forgetCurveItem = new ForgetCurveItem();
        forgetCurveItem.setCycle(cycle);
        forgetCurveItem.setTargetId(id);
        forgetCurveItem.setDateTime(time);
        forgetCurveItem.setStatus(0);
        ForgetCurveItem save = forgetCurveItemRepository.save(forgetCurveItem);
    }


}
