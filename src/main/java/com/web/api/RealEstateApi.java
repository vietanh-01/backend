package com.web.api;

import com.web.dto.request.RealEstateRequest;
import com.web.dto.request.RealEstateRequestSearch;
import com.web.dto.response.RealEstateProvinceDto;
import com.web.dto.response.RealEstateResponse;
import com.web.elasticsearch.model.RealEstateSearch;
import com.web.entity.RealEstate;
import com.web.enums.Status;
import com.web.service.RealEstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/real-estate")
@CrossOrigin("*")
public class RealEstateApi {

    @Autowired
    private RealEstateService realEstateService;

    @PostMapping("/all/create-update")
    public ResponseEntity<?> save(@RequestBody RealEstateRequest request){
        RealEstateResponse result = realEstateService.saveOrUpdate(request);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/all/my-real-estate")
    public ResponseEntity<?> myRealEstate(@RequestParam(required = false) Status status, Pageable pageable){
        Page<RealEstate> result = realEstateService.myRealEstate(status,pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/all/delete")
    public ResponseEntity<?> delete(@RequestParam(value = "id") Long id){
        realEstateService.deleteByUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<?> deleteByAdmin(@RequestParam(value = "id") Long id){
        realEstateService.deleteByAdmin(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/admin/update-status")
    public ResponseEntity<?> updateStatus(@RequestParam Status status,@RequestParam(value = "id") Long id){
        realEstateService.updateStatus(status,id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/admin/count-today")
    public ResponseEntity<?> countToday(){
        Long result = realEstateService.countToday();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/admin/total-post")
    public ResponseEntity<?> totalPost(){
        Long result = realEstateService.totalPost();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/admin/real-estate-province")
    public ResponseEntity<?> realEstateProvince(){
        List<RealEstateProvinceDto> result = realEstateService.numPostProvince();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/admin/accuracy")
    public ResponseEntity<?> accuracy(@RequestParam(value = "id") Long id){
        realEstateService.accuracy(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/public/search-full-accuracy")
    public ResponseEntity<?> searchFullAccuracy(@RequestBody RealEstateRequestSearch search, Pageable pageable){
        Page<RealEstateSearch> result = realEstateService.searchFullElasticsearch(pageable, search.getCategoryIds(),
                search.getMinPrice(), search.getMaxPrice(), search.getMinAcreage(), search.getMaxAcreage(), search.getProvinceId(), search.getDistrictsId(),
                search.getProjectName(), search.getAccuracy());
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/public/get-full")
    public ResponseEntity<?> getFull(Pageable pageable){
        Page<RealEstateSearch> result = realEstateService.fullRealEstateSearch(pageable);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/public/same-price")
    public ResponseEntity<?> samePrice(@RequestParam Double price, @RequestParam Long id){
        List<RealEstateResponse> result = realEstateService.samePrice(price, id);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/admin/all")
    public ResponseEntity<?> allRealEstate(@RequestParam(required = false) Status status, Pageable pageable){
        Page<RealEstate> result = realEstateService.all(status, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/user/count-real-estate")
    public Long countByUser(){
        return realEstateService.countByUser();
    }

    @GetMapping("/public/find-by-id")
    public RealEstate findById(@RequestParam Long id){
        return realEstateService.findById(id);
    }

    @GetMapping("/public/find-by-id-public")
    public RealEstateResponse findByIdForPublic(@RequestParam Long id){
        return realEstateService.findByIdForPublicUser(id);
    }

    @PostMapping("/user/extend-expired-date")
    public ResponseEntity<?> extendExpiredDate(@RequestParam Long id, @RequestParam Integer numDay){
        realEstateService.extendExpiredDate(id, numDay);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/admin/tranfer")
    public void tranfer(){
        realEstateService.tranfer();
    }
}
