package com.yuman.repository;

import com.yuman.entity.ForgetCurveItem;
import com.yuman.entity.FourReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface FourReviewRepository extends JpaRepository<FourReview, String> {



    @Transactional
    public int deleteFourReviewByTargetId(String targetId);

    @Transactional
    @Modifying
    @Query("update FourReview m set m.status=?2 where m.id=?1")
    public int finish(String id, Integer status);


    @Query(value = "select date_time from four_review where status = 0 and target_id = ?1 order by date_time limit 1 ", nativeQuery = true)
    public Date findNextReviewTime(String id);

}
