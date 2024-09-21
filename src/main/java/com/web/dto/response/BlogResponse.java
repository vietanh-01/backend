package com.web.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
public class BlogResponse {

    private Long id;

    private Date createdDate;

    private Time createdTime;

    private String title;

    private String description;

    private String content;

    private String image;

    private Integer numView;

    private UserResponse user;
}
