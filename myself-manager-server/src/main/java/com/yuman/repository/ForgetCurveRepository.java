package com.yuman.repository;

import com.yuman.entity.ForgetCurve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ForgetCurveRepository extends JpaRepository<ForgetCurve, String> {

    @Transactional
    @Modifying
    @Query("update ForgetCurve m set m.state=?2 where m.id=?1")
    public int updateStatus(String id,Integer state);

}
