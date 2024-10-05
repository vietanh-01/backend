package com.web.api;
import com.web.entity.HistoryPay;
import com.web.repository.HistoryPayRepository;
import com.web.service.HistoryPayService;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/history-pay")
@CrossOrigin
public class HistoryPayApi {

    @Autowired
    HistoryPayService historyPayService;

    @GetMapping("/admin/all")
    public Page<HistoryPay> findbyAdmin(@RequestParam(value = "start", required = false) Date start,
                                        @RequestParam(value = "end", required = false) Date end,
                                        @RequestParam(value = "search", required = false) String search, Pageable pageable){
        Page<HistoryPay> result = historyPayService.findbyAdmin(start, end, pageable, search);
        return result;
    }

    @GetMapping("/admin/find-by-user")
    public List<HistoryPay> findByUserId(@RequestParam(value = "userId") Long userId){
        return historyPayService.findByUserId(userId);
    }

    @GetMapping("/admin/find-by-year")
    public List<Double> statistic(@RequestParam("year") Integer year){
        List<Double> list = historyPayService.doanhThu(year);
        return list;
    }

    @GetMapping("/all/my-history-pay")
    public List<HistoryPay> findByUser(){
        List<HistoryPay> list = historyPayService.findByUser();
        return list;
    }
}
