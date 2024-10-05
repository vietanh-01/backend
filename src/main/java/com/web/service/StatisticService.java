package com.web.service;

import com.web.dto.response.RealEstateProvinceDto;
import com.web.enums.Status;
import com.web.repository.HistoryPayRepository;
import com.web.repository.RealEstateRepository;
import com.web.repository.UserRepository;
import com.web.utils.Contains;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Component
public class StatisticService {

    @Autowired
    private HistoryPayRepository historyPayRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RealEstateRepository realEstateRepository;

    public Long doanhThuThangNay(){
        Long result = historyPayRepository.doanhThuThangNay();
        return result;
    }

    public Long doanhThuHomNay(){
        Long result = historyPayRepository.doanhThuHomNay();
        return result;
    }

    public Long soLuongUser(){
        Long result = userRepository.countUserByRole(Contains.ROLE_USER);
        return result;
    }

    public Long soLuongBds(){
        Long result = realEstateRepository.count();
        return result;
    }

    public List<RealEstateProvinceDto> soLuongBdsCacTinh(){
        List<RealEstateProvinceDto> list = new ArrayList<>();
        List<Object[]> objs = realEstateRepository.soLuongBdsCacTinh();
        for(Object[] o : objs){
            RealEstateProvinceDto re = new RealEstateProvinceDto();
            re.setTenTinh((String) o[0]);
            re.setSoLuongBds((BigInteger) o[1]);
            list.add(re);
        }
        return list;
    }

    public List<Long> doanhThuNam(@RequestParam("nam") Integer nam){
        List<Long> list = new ArrayList<>();
        for(int i=1; i< 13; i++){
            Long tong = historyPayRepository.tinhDoanhThuNam(i, nam);
            list.add(tong);
        }
        return list;
    }

    public Long[] tinViPham(){
        Long[] result = new Long[2];
        Long tongTinViPham = realEstateRepository.countViPham(Status.VI_PHAM);
        result[1] = tongTinViPham;
        Long tongTin = realEstateRepository.count();
        result[0] = tongTin-tongTinViPham;
        return result;
    }
}
