package com.web.service;

import com.web.entity.RealEstateImage;
import com.web.exception.MessageException;
import com.web.repository.RealEstateImageRepository;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RealEstateImageService {

    @Autowired
    RealEstateImageRepository realEstateImageRepository;

    @Autowired
    private UserUtils userUtils;

    public void delete(Long id){
        RealEstateImage realEstateImage = realEstateImageRepository.findById(id).get();
        if(realEstateImage.getRealEstate().getUser().getId() != userUtils.getUserWithAuthority().getId()){
            throw new MessageException("Bạn không đủ quyền");
        }
        realEstateImageRepository.deleteById(id);
    }
}
