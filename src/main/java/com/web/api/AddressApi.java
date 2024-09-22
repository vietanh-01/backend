package com.web.api;

import com.web.entity.Province;
import com.web.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
@CrossOrigin
public class AddressApi {

    @Autowired
    private ProvinceRepository provinceRepository;

    @GetMapping("/public/province")
    public List<Province> findAllProvince(){
        return provinceRepository.findAll();
    }

}
