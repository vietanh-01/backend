package com.web.repository;

import com.web.entity.RealEstateCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface RealEstateCategoryRepository extends JpaRepository<RealEstateCategory, Long> {

    @Modifying
    @Transactional
    @Query("delete from RealEstateCategory p where p.realEstate.id = ?1")
    int deleteByRealEstate(Long realEstateId);
}
