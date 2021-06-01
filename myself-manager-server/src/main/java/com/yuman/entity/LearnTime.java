package com.yuman.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "learn_time")
public class LearnTime {


    @Id
    @GenericGenerator(name = "idGenerator",strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    /**
     * 学习名称
     */
    @Column(name = "learn_name")
    private String learnName;


    /**
     * 学习时长
     */
    @Column(name = "learn_time")
    private Integer learnTime;



    @TableField(exist = false)
    @Transient
    private String learnTimeText;

}
