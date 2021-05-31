package com.yuman.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuman.entity.LearnTime;
import com.yuman.entity.TinyHabit;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LearnTimeMapper extends BaseMapper<LearnTime> {
}
