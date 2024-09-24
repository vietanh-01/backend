package com.web.mapper;

import com.web.dto.request.RealEstateRequest;
import com.web.dto.request.UserRequest;
import com.web.entity.RealEstate;
import com.web.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RealEstateMapper {

    @Autowired
    private ModelMapper mapper;

    public RealEstate requestToEntity(RealEstateRequest request){
        RealEstate realEstate = mapper.map(request, RealEstate.class);
        return realEstate;
    }
}
