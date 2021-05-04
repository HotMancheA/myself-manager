package com.yuman.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuman.entity.TinyHabit;
import com.yuman.entity.TinyHabitLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

@Mapper
public interface TinyHabitLogMapper extends BaseMapper<TinyHabitLog> {

  
}
