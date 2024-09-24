package com.web.service;

import com.web.entity.Report;
import com.web.repository.ReportRepository;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.List;

@Component
public class ReportService {

    @Autowired
    public UserUtils userUtils;

    @Autowired
    ReportRepository reportRepository;

    public Report save(Report report){
        report.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        reportRepository.save(report);
        return report;
    }

    public List<Report> findByBds(Long bdsId){
        return reportRepository.findByBds(bdsId);
    }
}
