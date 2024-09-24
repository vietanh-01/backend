package com.web.mapper;

import com.web.dto.response.BlogResponse;
import com.web.dto.response.DistrictDto;
import com.web.dto.response.ProvinceDto;
import com.web.dto.response.WardDto;
import com.web.entity.Blog;
import com.web.entity.Wards;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    @Autowired
    private ModelMapper mapper;

    public WardDto entityToDto(Wards wards){
        WardDto wardDto = new WardDto();
        wardDto.setId(wards.getId());
        wardDto.setName(wards.getName());
        DistrictDto districtDto = new DistrictDto();
        districtDto.setId(wards.getDistricts().getId());
        districtDto.setName(wards.getDistricts().getName());
        ProvinceDto provinceDto = new ProvinceDto();
        provinceDto.setId(wards.getDistricts().getProvince().getId());
        provinceDto.setName(wards.getDistricts().getProvince().getName());
        districtDto.setProvince(provinceDto);
        wardDto.setDistrict(districtDto);
        return wardDto;
    }
}
