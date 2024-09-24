package com.web.repository;

import com.web.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long> {

    @Query("select r from Report r where r.realEstate.id = ?1")
    public List<Report> findByBds(Long bdsId);
}
