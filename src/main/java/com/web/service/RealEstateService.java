package com.web.service;

import com.web.dto.request.RealEstateRequest;
import com.web.dto.response.RealEstateProvinceDto;
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
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

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

    public void updateStatus(Status status,Long id){
        RealEstate realEstate = realEstateRepository.findById(id).get();
        realEstate.setStatus(status);
        realEstateRepository.save(realEstate);
        RealEstateResponse realEstateResponse = realEstateMapper.entityToResponse(realEstate);
        RealEstateSearch realEstateSearch = realEstateMapper.responseToSearch(realEstateResponse);
        realEstateSearchRepository.save(realEstateSearch);
    }

    // đếm số lượng bài đăng hôm nay
    public Long countToday(){
        Long count = realEstateRepository.soBaiDanghomNay(new Date(System.currentTimeMillis()));
        return count;
    }

    public Long totalPost(){
        Long total = realEstateRepository.count();
        return total;
    }

    // số lượng bds trong các tỉnh
    public List<RealEstateProvinceDto> numPostProvince(){
        List<RealEstateProvinceDto> list = new ArrayList<>();
        List<Object[]> objs = realEstateRepository.soLuongBdsCacTinh();
        for(Object[] o : objs){
            RealEstateProvinceDto re = new RealEstateProvinceDto();
            re.setTenTinh((String) o[0]);
            re.setSoLuongBds((BigInteger) o[1]);
            list.add(re);
        }
        return list;
    }

    public void accuracy(@RequestParam("id") Long id){
        RealEstate realEstate = realEstateRepository.findById(id).get();
        if(realEstate.getAccuracy() == false){
            realEstate.setAccuracy(true);
        }
        else{
            realEstate.setAccuracy(false);
        }
        realEstateRepository.save(realEstate);
        RealEstateResponse realEstateResponse = realEstateMapper.entityToResponse(realEstate);
        RealEstateSearch realEstateSearch = realEstateMapper.responseToSearch(realEstateResponse);
        realEstateSearchRepository.save(realEstateSearch);
    }

    public Page<RealEstateSearch> searchFullElasticsearch(Pageable pageable,List<Long> categoryIds, Double minPrice, Double maxPrice,
                                                          Float minAcreage, Float maxAcreage, Long provinceId, List<Long> districtsId) {

        if(minPrice == null || maxPrice == null){
            minPrice = 0D;
            maxPrice = 1000000000000D;
        }
        if(minAcreage == null || maxAcreage == null){
            minAcreage = 0F;
            maxAcreage = 100000F;
        }
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("price").gte(minPrice).lte(maxPrice));
        boolQuery.must(QueryBuilders.rangeQuery("acreage").gte(minAcreage).lte(maxAcreage));

        if(categoryIds.size() > 0){
            BoolQueryBuilder blcategory = QueryBuilders.boolQuery();
            categoryIds.forEach(p->{
                blcategory.should(QueryBuilders.termQuery("realEstateCategories.category.id", p));
            });
            boolQuery.must(blcategory);
        }

        if(provinceId != null){
            boolQuery.must(QueryBuilders.termQuery("ward.district.province.id",provinceId));
            if(districtsId.size() > 0){
                BoolQueryBuilder blcategory = QueryBuilders.boolQuery();
                districtsId.forEach(d->{
                    blcategory.should(QueryBuilders.termQuery("ward.district.id", d));
                });
                boolQuery.must(blcategory);
            }
        }

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .withPageable(pageable)
                .build();

        SearchHits<RealEstateSearch> results = elasticsearchRestTemplate.search(nativeSearchQuery, RealEstateSearch.class, IndexCoordinates.of("real_estate"));
        List<RealEstateSearch> realEstateSearches = results.getSearchHits().stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());
        return new PageImpl<>(realEstateSearches, pageable, results.getTotalHits());
    }
}
