package com.web.api;

import com.web.entity.Report;
import com.web.repository.ReportRepository;
import com.web.service.ReportService;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/report")
@CrossOrigin("*")
public class ReportApi {


    @Autowired
    ReportService reportService;

    @PostMapping("/public/add")
    public ResponseEntity<?> save(@RequestBody Report report){
        Report result = reportService.save(report);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/admin/find-by-real-estate")
    public List<Report> findByBds(@RequestParam("id") Long bdsId){
        List<Report> list = reportService.findByBds(bdsId);
        return list;
    }

}