package com.yuman.repository;

import com.yuman.entity.LearnTime;
import com.yuman.entity.TinyHabit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearnTimeRepository extends JpaRepository<LearnTime, String> {



}
