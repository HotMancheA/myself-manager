package com.yuman.repository;

import com.yuman.entity.ForgetCurve;
import com.yuman.entity.ForgetCurveItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface ForgetCurveItemRepository extends JpaRepository<ForgetCurveItem, String> {



    @Transactional
    public int deleteForgetCurveItemByTargetId(String targetId);

    @Transactional
    @Modifying
    @Query("update ForgetCurveItem m set m.status=?2 where m.id=?1")
    public int finish(String id,Integer status);


    @Query(value = "select date_time from forget_curve_item where status = 0 and target_id = ?1 order by date_time limit 1 ", nativeQuery = true)
    public Date findNextReviewTime(String id);

}
