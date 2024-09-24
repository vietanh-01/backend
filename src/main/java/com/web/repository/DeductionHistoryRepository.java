package com.web.repository;

import com.web.entity.DeductionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface DeductionHistoryRepository extends JpaRepository<DeductionHistory, Long> {

    @Query("select d from DeductionHistory d where d.user.id=  ?1")
    public List<DeductionHistory> findByUser(Long userId);

    @Query("select d from DeductionHistory d where d.createdDate >= ?1 and d.createdDate <= ?2")
    public Page<DeductionHistory> findByDate(Date from , Date to, Pageable pageable);
}
