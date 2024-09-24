package com.web.service;

import com.web.dto.request.RealEstateRequest;
import com.web.dto.response.RealEstateResponse;
import com.web.elasticsearch.model.RealEstateSearch;
import com.web.elasticsearch.repository.RealEstateSearchRepository;
import com.web.entity.*;
import com.web.enums.Status;
import com.web.exception.MessageException;
import com.web.mapper.RealEstateMapper;
import com.web.repository.*;
import com.web.utils.Contains;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Component
public class RealEstateService {

    @Autowired
    private RealEstateRepository realEstateRepository;

    @Autowired
    private RealEstateImageRepository realEstateImageRepository;

    @Autowired
    private RealEstateCategoryRepository realEstateCategoryRepository;

    @Autowired
    private DeductionHistoryRepository deductionHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JuridicalRepository juridicalRepository;

    @Autowired
    private WardsRepository wardsRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private RealEstateMapper realEstateMapper;

    @Autowired
    private RealEstateSearchRepository realEstateSearchRepository;

    @Value("${paycost}")
    private Double payCost;

    public RealEstateResponse saveOrUpdate(RealEstateRequest request){
        User user = userUtils.getUserWithAuthority();
        if(request.getId() == null){
            if(user.getAmount() == null){
                throw new MessageException("Không đủ số dư");
            }
            if(user.getAmount() < payCost){
                throw new MessageException("Không đủ số dư");
            }
        }
        RealEstate realEstate = realEstateMapper.requestToEntity(request);
        Juridical juridical = juridicalRepository.findById(request.getJuridical().getId()).get();
        Wards wards = wardsRepository.findById(request.getWards().getId()).get();

        realEstate.setJuridical(juridical);
        realEstate.setWards(wards);
        if(realEstate.getId() != null){
            RealEstate re = realEstateRepository.findById(realEstate.getId()).get();
            if(re.getUser().getId() != user.getId() && !user.getAuthorities().getName().equals(Contains.ROLE_ADMIN)){
                throw new MessageException("Bạn không đủ quyền");
            }
            realEstate.setCreatedDate(re.getCreatedDate());
            realEstate.setCreatedTime(re.getCreatedTime());
            realEstate.setStatus(re.getStatus());
            realEstate.setUser(re.getUser());
            realEstate.setNumView(re.getNumView());
            realEstate.setAccuracy(re.getAccuracy());
        }
        else{
            realEstate.setCreatedDate(new Date(System.currentTimeMillis()));
            realEstate.setCreatedTime(new Time(System.currentTimeMillis()));
            realEstate.setUser(user);
            realEstate.setStatus(Status.DANG_CHO_DUYET);
            realEstate.setNumView(0);
            realEstate.setAccuracy(false);
        }
        RealEstate result = realEstateRepository.save(realEstate);

        List<RealEstateImage> realEstateImages = new ArrayList<>();
        for(String s : request.getListImages()){
            RealEstateImage image = new RealEstateImage();
            image.setImage(s);
            image.setRealEstate(result);
            realEstateImageRepository.save(image);
            realEstateImages.add(image);
        }
        result.setRealEstateImages(realEstateImages);


        realEstateCategoryRepository.deleteByRealEstate(result.getId());
        List<RealEstateCategory> realEstateCategories = new ArrayList<>();
        for(Long id : request.getCategoryId()){
            RealEstateCategory realEstateCategory = new RealEstateCategory();
            Category category = categoryRepository.findById(id).get();
            realEstateCategory.setCategory(category);
            realEstateCategory.setRealEstate(result);
            realEstateCategoryRepository.save(realEstateCategory);
            realEstateCategories.add(realEstateCategory);
        }
        result.setRealEstateCategories(realEstateCategories);

        if(request.getId() == null){
            user.setAmount(user.getAmount() - payCost);
            userRepository.save(user);
            DeductionHistory deductionHistory = new DeductionHistory();
            deductionHistory.setCreatedDate(new Date(System.currentTimeMillis()));
            deductionHistory.setCreatedTime(new Time(System.currentTimeMillis()));
            deductionHistory.setDeductedAmount(payCost);
            deductionHistory.setUser(user);
            deductionHistory.setRealEstateId(result.getId());
            deductionHistory.setRealEstateTitle(result.getTitle());
            deductionHistoryRepository.save(deductionHistory);
        }
        RealEstateResponse response = realEstateMapper.entityToResponse(result);
        RealEstateSearch realEstateSearch = realEstateMapper.responseToSearch(response);
        realEstateSearchRepository.save(realEstateSearch);
        return response;
    }

    public List<RealEstate> myRealEstate(Status status){
        List<RealEstate> list = null;
        User user = userUtils.getUserWithAuthority();
        if(status == null){
            list = realEstateRepository.findByUser(user.getId());
        }
        else{
            list = realEstateRepository.findByUserAndStatus(user.getId(), status);
        }
        return list;
    }

    public void deleteByUser(Long id){
        RealEstate realEstate = realEstateRepository.findById(id).get();
        if(userUtils.getUserWithAuthority().getId() != realEstate.getUser().getId()){
            throw new MessageException("Bạn không đủ quyền");
        }
        realEstateRepository.delete(realEstate);
        realEstateSearchRepository.deleteById(id);
    }
}
