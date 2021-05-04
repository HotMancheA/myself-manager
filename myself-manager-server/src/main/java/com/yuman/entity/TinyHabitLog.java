package com.yuman.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tiny_habit_log")
public class TinyHabitLog {


    @Id
    @GenericGenerator(name = "idGenerator",strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "tiny_habit_id")
    private String tinyHabitId;

    /**
     * 打卡状态
     * 0 未打卡
     * 1 打卡中
     * 2 打卡完成
     *
     */
    @Column(name = "punch_card_state")
    private Integer punchCardState;

    @Column(name = "start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date startTime;

    @Column(name = "end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date endTime;

    //打卡间隔
    @Column(name = "time_interval")
    private Integer timeInterval;

    //单位名称
    @Column(name = "unit_name")
    private String unitName;

    //执行次数
    @Column(name = "execute_count")
    private Integer executeCount;

    @TableField(exist = false)
    @Transient
    private String punchCardStateText;
}
