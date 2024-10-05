package com.web.repository;

import com.web.entity.HistoryPay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryPayRepository extends JpaRepository<HistoryPay, Long> {

    @Query("select h from HistoryPay h where h.createdDate >= ?1 and h.createdDate <= ?2 and (h.user.email like ?3 or h.user.username like ?3) order by h.id desc ")
    Page<HistoryPay> findByAdmin(Date start, Date end,String search, Pageable pageable);

    @Query(value = "select sum(i.total_amount) from history_pay i where Month(i.created_date) = ?1 and Year(i.created_date) = ?2", nativeQuery = true)
    public Double tinhDoanhThu(Integer thang, Integer month);

    @Query(value = "select sum(i.total_amount) from history_pay i where Month(i.created_date) = ?1 and Year(i.created_date) = ?2", nativeQuery = true)
    public Long tinhDoanhThuNam(Integer thang, Integer month);

    @Query("select h from HistoryPay h where h.orderId = ?1 and h.requestId = ?2")
    Optional<HistoryPay> findByOrderIdAndRequestId(String orderid, String requestId);

    @Query("select h from HistoryPay h where h.user.id = ?1")
    List<HistoryPay> findByUser(Long userId);

    @Query("select sum(h.totalAmount) from HistoryPay h where MONTH(h.createdDate) = month(current_date) and YEAR(h.createdDate) = YEAR(current_date) ")
    Long doanhThuThangNay();

    @Query("select sum(h.totalAmount) from HistoryPay h where h.createdDate = current_date")
    Long doanhThuHomNay();
}
