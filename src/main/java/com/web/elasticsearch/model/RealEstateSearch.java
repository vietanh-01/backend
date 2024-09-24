package com.web.elasticsearch.model;
import com.web.dto.response.UserDto;
import com.web.dto.response.WardDto;
import com.web.entity.Juridical;
import com.web.entity.RealEstateCategory;
import com.web.entity.RealEstateImage;
import com.web.enums.Status;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(indexName = "real_estate")
@Getter
@Setter
public class RealEstateSearch {

    @Id
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

    private UserDto user;

    private WardDto ward;

    private List<RealEstateImage> realEstateImages = new ArrayList<>();

    private List<RealEstateCategory> realEstateCategories = new ArrayList<>();
}
