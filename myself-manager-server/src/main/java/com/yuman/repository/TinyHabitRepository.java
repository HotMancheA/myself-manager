package com.yuman.repository;

import com.yuman.entity.TinyHabit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TinyHabitRepository extends JpaRepository<TinyHabit, String> {



}
