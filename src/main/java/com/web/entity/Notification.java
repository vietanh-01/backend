package com.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String title;

    private LocalDateTime createdDate;

    private Boolean isRead = false;

    private String link;

    @ManyToOne
    private User user;
}
