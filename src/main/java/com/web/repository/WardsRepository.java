package com.web.repository;

import com.web.entity.Wards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WardsRepository extends JpaRepository<Wards, Long> {

    @Query("select d from Wards d where d.districts.id = ?1")
    public List<Wards> findByDis(Long disId);


}