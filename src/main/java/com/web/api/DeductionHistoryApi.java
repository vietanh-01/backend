package com.web.api;
import com.web.entity.DeductionHistory;
import com.web.entity.User;
import com.web.repository.DeductionHistoryRepository;
import com.web.service.DeductionHistoryService;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/deduction-history")
@CrossOrigin("*")
public class DeductionHistoryApi {

    @Autowired
    DeductionHistoryService deductionHistoryService;

    @GetMapping("/user/my-DeductionHistory")
    public List<DeductionHistory> findByUser(){
        List<DeductionHistory> list = deductionHistoryService.findByUser();
        return list;
    }

    @GetMapping("/admin/all-DeductionHistory")
    public Page<DeductionHistory> findAll(@RequestParam(value = "from", required = false)Date from,
                                          @RequestParam(value = "to", required = false)Date to, Pageable pageable){
        return deductionHistoryService.findAll(from, to, pageable);
    }

    @GetMapping("/admin/find-DeductionHistory-by-user")
    public List<DeductionHistory> findByUserId(@RequestParam(value = "userId", required = false) Long userId){
        return deductionHistoryService.findByUserId(userId);
    }
}
