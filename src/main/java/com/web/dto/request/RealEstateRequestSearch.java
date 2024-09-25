package com.web.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RealEstateRequestSearch {

    private List<Long> categoryIds = new ArrayList<>();

    private Double minPrice;

    private Double maxPrice;

    private Float minAcreage;

    private Float maxAcreage;

    private Long provinceId;

    private List<Long> districtsId = new ArrayList<>();
}
