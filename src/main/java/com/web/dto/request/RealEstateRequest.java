package com.web.dto.request;

import com.web.entity.Juridical;
import com.web.entity.Wards;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RealEstateRequest {

    private Long id;

    private String title;

    private String description;

    private String image;

    private Double price;

    private Float acreage;

    private Juridical juridical;

    private Integer roomNumber;

    private Integer toiletNumber;

    private String linkMap;

    private String projectName;

    private Float facade;

    private Float numFloors;

    private Float road;

    private Wards wards;

    private List<String> listImages = new ArrayList<>();

    private List<Long> categoryId = new ArrayList<>();
}
