package com.web.api;

import com.web.dto.request.BlogRequest;
import com.web.dto.request.RealEstateRequest;
import com.web.dto.response.BlogResponse;
import com.web.entity.RealEstate;
import com.web.service.BlogService;
import com.web.service.RealEstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/real-estate")
@CrossOrigin("*")
public class RealEstateApi {

    @Autowired
    private RealEstateService realEstateService;

    @PostMapping("/all/create-update")
    public ResponseEntity<?> save(@RequestBody RealEstateRequest request){
        RealEstate result = realEstateService.saveOrUpdate(request);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }


}
