package com.web.repository;

import com.web.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
}