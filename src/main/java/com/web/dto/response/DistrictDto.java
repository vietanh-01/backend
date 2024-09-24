package com.web.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistrictDto {

    private Long id;

    private String name;

    private ProvinceDto province;
}
