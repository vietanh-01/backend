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
    private ReportRepository reportRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private RealEstateMapper realEstateMapper;

    @Autowired
    private RealEstateSearchRepository realEstateSearchRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    NotificationService notificationService;

    @Value("${paycost}")
    private Double payCost;

    @Value("${url.realestate}")
    private String urlRealEstate;

    public RealEstateResponse saveOrUpdate(RealEstateRequest request){
        User user = userUtils.getUserWithAuthority();
        if(user.getAmount() == null){
            throw new MessageException("Không đủ số dư");
        }
        if(user.getAmount() < payCost){
            throw new MessageException("Không đủ số dư");
        }

        RealEstate realEstate = realEstateMapper.requestToEntity(request);
        Juridical juridical = juridicalRepository.findById(request.getJuridical().getId()).get();
        Wards wards = wardsRepository.findById(request.getWards().getId()).get();

        realEstate.setJuridical(juridical);
        realEstate.setWards(wards);

        List<RealEstateImage> realEstateImagesEx = new ArrayList<>();

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
            realEstate.setExpiredDate(re.getExpiredDate());
            realEstateImagesEx.addAll( re.getRealEstateImages());
        }
        else{
            realEstate.setCreatedDate(new Date(System.currentTimeMillis()));
            realEstate.setCreatedTime(new Time(System.currentTimeMillis()));
            realEstate.setUser(user);
            realEstate.setStatus(Status.DANG_HIEN_THI);
            realEstate.setNumView(0);
            realEstate.setAccuracy(false);
            Long curLong = System.currentTimeMillis();
            Long nextDate = curLong + (1000L * 60L * 60L * 24L * 7L);
            Date expiredDate = new Date(nextDate);
            realEstate.setExpiredDate(expiredDate);
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
        realEstateImages.addAll(realEstateImagesEx);
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

        user.setAmount(user.getAmount() - payCost);
        userRepository.save(user);
        DeductionHistory deductionHistory = new DeductionHistory();
        deductionHistory.setCreatedDate(new Date(System.currentTimeMillis()));
        deductionHistory.setCreatedTime(new Time(System.currentTimeMillis()));
        deductionHistory.setDeductedAmount(payCost);
        deductionHistory.setUser(user);
        deductionHistory.setRealEstateId(result.getId());
        if(request.getId() == null){
            deductionHistory.setRealEstateTitle("Đăng tin: "+result.getTitle());
        }
        else{
            deductionHistory.setRealEstateTitle("Cập nhật tin: "+result.getTitle());
        }
        deductionHistoryRepository.save(deductionHistory);

        System.out.println("size be: "+result.getRealEstateImages().size());
        RealEstateResponse response = realEstateMapper.entityToResponse(result);
        response.setRealEstateImages(result.getRealEstateImages());
        System.out.println("size img: "+realEstateImagesEx.size());
        System.out.println("list img response: "+response.getRealEstateImages().size());
        RealEstateSearch realEstateSearch = realEstateMapper.responseToSearch(response);
        realEstateSearchRepository.save(realEstateSearch);

        if(request.getId() == null){
            notificationService.save("Tin đăng "+response.getId()+" đã được tạo",urlRealEstate,"Có tin đăng mới");
        }
        else{
            notificationService.save("Tin đăng "+response.getId()+" đã được cập nhật",urlRealEstate,"Có tin đăng được cập nhật");
        }
        for(RealEstateImage rm : realEstateImages){
            rm.setRealEstate(result);
        }
        for(RealEstateCategory rm : realEstateCategories){
            rm.setRealEstate(result);
        }
        realEstateImageRepository.saveAll(realEstateImages);
        realEstateCategoryRepository.saveAll(realEstateCategories);
        return response;
    }

    public Page<RealEstate> myRealEstate(Status status, Pageable pageable){
        Page<RealEstate> list = null;
        User user = userUtils.getUserWithAuthority();
        if(status == null){
            list = realEstateRepository.findByUser(user.getId(), pageable);
        }
        else{
            list = realEstateRepository.findByUserAndStatus(user.getId(), status,pageable);
        }
        return list;
    }

    public Page<RealEstate> all(Status status, Pageable pageable){
        Page<RealEstate> page = null;
        if(status == null){
            page = realEstateRepository.findAll(pageable);
        }
        else{
            page = realEstateRepository.findByStatus(status, pageable);
        }
        return page;
    }

    public void deleteByUser(Long id){
        RealEstate realEstate = realEstateRepository.findById(id).get();
        if(userUtils.getUserWithAuthority().getId() != realEstate.getUser().getId()){
            throw new MessageException("Bạn không đủ quyền");
        }
        reportRepository.deleteByRealEstate(id);
        favoriteRepository.deleteByRealEstate(id);
        realEstateRepository.delete(realEstate);
        realEstateSearchRepository.deleteById(id);
    }

    public void deleteByAdmin(Long id){
        reportRepository.deleteByRealEstate(id);
        favoriteRepository.deleteByRealEstate(id);
        realEstateRepository.deleteById(id);
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

    public RealEstate findById(Long id){
        return realEstateRepository.findById(id).get();
    }

    public RealEstateResponse findByIdForPublicUser(Long id){
        return realEstateMapper.entityToResponse(realEstateRepository.findById(id).get());
    }

    public void extendExpiredDate(Long id, Integer numDay){
        RealEstate realEstate = realEstateRepository.findById(id).get();
        User user = userUtils.getUserWithAuthority();
        if(realEstate.getUser().getId() != user.getId()){
            throw new MessageException("Bạn không đủ quyền");
        }
        Double amount = payCost * (numDay/7);
        if(user.getAmount() == null){
            throw new MessageException("Ví của bạn không đủ số dư để thực hiện chức năng này");
        }
        if(user.getAmount() < amount){
            throw new MessageException("Ví của bạn không đủ số dư để thực hiện chức năng này");
        }
        Long longDate = realEstate.getExpiredDate().getTime();
        longDate = longDate + (1000L * 60L * 60L * 24L * numDay);
        Date ex = new Date(longDate);
        realEstate.setExpiredDate(ex);
        realEstateRepository.save(realEstate);
        user.setAmount(user.getAmount() - amount);
        userRepository.save(user);
        RealEstateSearch realEstateSearch = realEstateSearchRepository.findById(id).get();
        realEstateSearch.setExpiredDate(new java.util.Date(longDate));
        realEstateSearchRepository.save(realEstateSearch);

        DeductionHistory deductionHistory = new DeductionHistory();
        deductionHistory.setRealEstateId(realEstate.getId());
        deductionHistory.setRealEstateTitle("Gia hạn "+numDay+ " ngày tin đăng: "+realEstate.getTitle());
        deductionHistory.setDeductedAmount(amount);
        deductionHistory.setDeductedAmount(amount);
        deductionHistory.setCreatedDate(new Date(System.currentTimeMillis()));
        deductionHistory.setCreatedTime(new Time(System.currentTimeMillis()));
        deductionHistory.setUser(user);
        deductionHistoryRepository.save(deductionHistory);
        notificationService.save("Tin đăng "+id+" đã được gia hạn "+numDay+ " ngày",urlRealEstate,"Có tin đăng được gia hạn");
    }

    public Long totalPost(){
        Long total = realEstateRepository.count();
        return total;
    }

    public Long countByUser(){
        User user = userUtils.getUserWithAuthority();
        Long soBaiDang = realEstateRepository.soBaiDangByUser(user.getId());
        return soBaiDang;
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
                                                          Float minAcreage, Float maxAcreage, Long provinceId, List<Long> districtsId,
                                                          String projectName, Boolean accuracy) {

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
        boolQuery.must(QueryBuilders.rangeQuery("expiredDate").gte(new java.util.Date(System.currentTimeMillis())));
        boolQuery.must(QueryBuilders.matchQuery("status", Status.DANG_HIEN_THI));

        if(categoryIds.size() > 0){
            BoolQueryBuilder blcategory = QueryBuilders.boolQuery();
            categoryIds.forEach(p->{
                blcategory.should(QueryBuilders.termQuery("realEstateCategories.category.id", p));
            });
            boolQuery.must(blcategory);
        }

        if(accuracy != null){
            boolQuery.must(QueryBuilders.matchQuery("accuracy", accuracy));
        }

        if(projectName != null){
            boolQuery.must(QueryBuilders.matchQuery("projectName", projectName));
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


    public Page<RealEstateSearch> fullRealEstateSearch(Pageable pageable) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("expiredDate").gte(new java.util.Date(System.currentTimeMillis())));
        boolQuery.must(QueryBuilders.matchQuery("status", Status.DANG_HIEN_THI));

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

    public List<RealEstateResponse> samePrice(Double price, Long id){
        Double min = price - (price * 10 / 100);
        Double max = price + (price * 10 / 100);
        System.out.println("Tiền min / m2: "+min);
        System.out.println("Tiền max / m2: "+max);
        List<RealEstate> realEstates = realEstateRepository.calSamePrice(min, max, Status.DANG_HIEN_THI, id);
        List<RealEstateResponse> result = realEstates.stream().map(r->realEstateMapper.entityToResponse(r)).collect(Collectors.toList());
        return result;
    }

    public void tranfer() {
        realEstateSearchRepository.deleteAll();
        List<RealEstate> realEstates = realEstateRepository.findAll();
        List<RealEstateResponse> realEstateResponses = realEstates.stream().map(re -> realEstateMapper.entityToResponse(re)).collect(Collectors.toList());
        List<RealEstateSearch> realEstateSearches = realEstateResponses.stream().map(re -> realEstateMapper.responseToSearch(re)).collect(Collectors.toList());
        realEstateSearchRepository.saveAll(realEstateSearches);
    }
}
