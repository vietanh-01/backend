package com.web.api;

import com.web.entity.RealEstateImage;
import com.web.repository.RealEstateImageRepository;
import com.web.service.RealEstateImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Real-Estate-Image")
@CrossOrigin("*")
public class RealEstateImageApi {

    @Autowired
    private RealEstateImageService realEstateImageService;

    @DeleteMapping("/user/delete")
    public void xoaPhong(@RequestParam(value = "id") Long id){
        realEstateImageService.delete(id);
    }
}
