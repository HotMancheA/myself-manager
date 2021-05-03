package com.yuman.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "forget_curve")
public class ForgetCurve {


    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    private String target;

    private String description;

    //状态
    private Integer state;

    @Column(name = "order_num")
    private Integer orderNum;

}
