package com.yuman.repository;

import com.yuman.entity.TinyHabitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface TinyHabitLogRepository extends JpaRepository<TinyHabitLog, String> {

    @Query(value = "select * from tiny_habit_log where date_format(start_time,'%Y-%m-%d') = ?1 and tiny_habit_id = ?2 limit 1 ", nativeQuery = true)
    public TinyHabitLog findByStartTime(String startTime, String tinyHabitId);

    @Query(value = "select count(id) from tiny_habit_log where date_format(end_time,'%Y-%m-%d') = ?1 and tiny_habit_id = ?2 limit 1 ", nativeQuery = true)
    public Integer findByEndTime(String endTime, String tinyHabitId);


    @Transactional
    public int deleteTinyHabitLogByTinyHabitId(String tinyHabitId);
}
