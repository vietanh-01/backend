package com.web.service;

import com.web.entity.DeductionHistory;
import com.web.entity.User;
import com.web.repository.DeductionHistoryRepository;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;

@Component
public class DeductionHistoryService {

    @Autowired
    DeductionHistoryRepository deductionHistoryRepository;

    @Autowired
    UserUtils userUtils;

    public List<DeductionHistory> findByUser(){
        User user = userUtils.getUserWithAuthority();
        return deductionHistoryRepository.findByUser(user.getId());
    }

    public Page<DeductionHistory> findAll(Date from, Date to, Pageable pageable){
        if(from == null || to == null){
            from = Date.valueOf("2000-01-01");
            to = Date.valueOf("2100-01-01");
        }
        return deductionHistoryRepository.findByDate(from, to, pageable);
    }

    public List<DeductionHistory> findByUserId(Long userId){
        return deductionHistoryRepository.findByUser(userId);
    }
}
