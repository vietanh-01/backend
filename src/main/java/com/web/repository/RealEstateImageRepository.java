package com.web.repository;

import com.web.entity.RealEstateImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealEstateImageRepository extends JpaRepository<RealEstateImage,Long> {
}
