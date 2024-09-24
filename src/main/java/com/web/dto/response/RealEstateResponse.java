package com.web.dto.response;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.web.entity.*;
import com.web.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RealEstateResponse {

    private Long id;

    private String title;

    private String description;

    private Date createdDate;

    private Time createdTime;

    private String image;

    private Double price;

    private Float acreage;

    private Boolean accuracy = false;

    private Status status;

    private Juridical juridical;

    private Integer roomNumber;

    private Integer toiletNumber;

    private String linkMap;

    private Integer numView;

    private String projectName;

    private Float facade;

    private Float numFloors;

    private Float road;

    private Boolean favorite = false;

    private UserDto user;

    private WardDto ward;

    private List<RealEstateImage> realEstateImages = new ArrayList<>();

    private List<RealEstateCategory> realEstateCategories = new ArrayList<>();
}
