package com.web.service;

import com.web.entity.RealEstate;
import com.web.entity.Report;
import com.web.repository.RealEstateRepository;
import com.web.repository.ReportRepository;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Component
public class ReportService {

    @Autowired
    public UserUtils userUtils;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    RealEstateRepository realEstateRepository;

    @Autowired
    NotificationService notificationService;

    @Value("${url.frontend}")
    String urlFrontend;

    public Report save(Report report){
        RealEstate realEstate = realEstateRepository.findById(report.getRealEstate().getId()).get();
        report.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        reportRepository.save(report);
        notificationService.save("Tin đăng: "+realEstate.getTitle()+" bị báo cáo","report","Có báo cáo tin đăng");
        return report;
    }

    public List<Report> findByBds(Long bdsId){
        return reportRepository.findByBds(bdsId);
    }

    public Page<Report> findAll(Pageable pageable, Date start, Date end){
        Page<Report> page = null;
        if(start == null || end == null){
            page = reportRepository.findAll(pageable);
        }
        else{
            page = reportRepository.findByDate(start, end, pageable);
        }
        return page;
    }

}
