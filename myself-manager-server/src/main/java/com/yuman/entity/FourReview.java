package com.yuman.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "four_review")
public class FourReview {

    public FourReview() {
    }

    public FourReview(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "target_id")
    private String targetId;

    private Integer cycle;

    @Column(name = "date_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date dateTime;

    private Integer status;

}
