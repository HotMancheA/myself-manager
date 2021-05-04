package com.yuman.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tiny_habit")
public class TinyHabit {


    @Id
    @GenericGenerator(name = "idGenerator",strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "task_name")
    private String taskName;


    private String description;

    //任务状态，0 关闭，1 开启
    private Integer state;
}
