package com.web.mapper;

import com.web.dto.request.RealEstateRequest;
import com.web.dto.request.UserRequest;
import com.web.dto.response.RealEstateResponse;
import com.web.entity.RealEstate;
import com.web.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RealEstateMapper {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AddressMapper addressMapper;

    public RealEstate requestToEntity(RealEstateRequest request){
        RealEstate realEstate = mapper.map(request, RealEstate.class);
        return realEstate;
    }

    public RealEstateResponse entityToResponse(RealEstate realEstate){
        RealEstateResponse response = mapper.map(realEstate, RealEstateResponse.class);
        response.setWard(addressMapper.entityToDto(realEstate.getWards()));
        return response;
    }
}
