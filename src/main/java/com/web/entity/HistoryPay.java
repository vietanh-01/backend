package com.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "history_pay")
@Getter
@Setter
public class HistoryPay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Double totalAmount;

    private String requestId;

    private String orderId;

    private Date createdDate;

    private Time createdTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
