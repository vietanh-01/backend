package com.web.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.web.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Entity
@Table(name = "Real_estate")
@Getter
@Setter
public class RealEstate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Date createdDate;

    private Time createdTime;

    private String image;

    private Double price;

    private Float acreage;

    private Boolean accuracy = false;

    @Column(name = "real_estate_status")
    private Status status;

    @ManyToOne
    private Juridical juridical;

    private Integer roomNumber;

    private Integer toiletNumber;

    private String linkMap;

    private Integer numView;

    private String projectName;

    private Float facade;

    private Float numFloors;

    private Float road;

    @Transient
    private Boolean favorite = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ward_id")
    private Wards wards;

    @OneToMany(mappedBy = "realEstate", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<RealEstateImage> realEstateImages;

    @OneToMany(mappedBy = "realEstate", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<RealEstateCategory> realEstateCategories;
}
