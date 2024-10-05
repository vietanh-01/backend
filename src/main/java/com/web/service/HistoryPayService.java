package com.web.service;

import com.web.entity.HistoryPay;
import com.web.repository.HistoryPayRepository;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Component
public class HistoryPayService {

    @Autowired
    HistoryPayRepository historyPayRepository;

    @Autowired
    UserUtils userUtils;

    public Page<HistoryPay> findbyAdmin(Date start, Date end, Pageable pageable,String search){
        if(start == null || end == null){
            start = Date.valueOf("2000-01-01");
            end = Date.valueOf("2100-01-01");
        }
        if(search == null){
            search = "";
        }
        return historyPayRepository.findByAdmin(start, end,"%"+search+"%", pageable);
    }

    public List<HistoryPay> findByUserId(Long userId){
        return historyPayRepository.findByUser(userId);
    }

    public List<Double> doanhThu(Integer nam){
        List<Double> list = new ArrayList<>();
        for(int i=1; i< 13; i++){
            Double tong = historyPayRepository.tinhDoanhThu(i, nam);
            list.add(tong);
        }
        return list;
    }

    public List<HistoryPay> findByUser(){
        return historyPayRepository.findByUser(userUtils.getUserWithAuthority().getId());
    }
}
