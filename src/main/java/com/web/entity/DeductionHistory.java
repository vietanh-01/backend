package com.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "deduction_history")
@Getter
@Setter
public class DeductionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Date createdDate;

    private Time createdTime;

    private Long realEstateId;

    private String realEstateTitle;

    private Double deductedAmount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

