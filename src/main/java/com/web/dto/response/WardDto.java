package com.web.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WardDto {

    private Long id;

    private String name;

    private DistrictDto district;
}
