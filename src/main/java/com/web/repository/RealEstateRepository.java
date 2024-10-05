package com.web.repository;

import com.web.entity.RealEstate;
import com.web.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface RealEstateRepository extends JpaRepository<RealEstate,Long> {

    @Query("select r from RealEstate r where r.createdDate >= ?1 and r.createdDate <= ?2")
    public List<RealEstate> findByDate(Date from, Date to);

    @Query("select r from RealEstate r where r.createdDate <= ?1 and r.status = ?2")
    public List<RealEstate> findByDate(Date date, Status status);

    @Query("select r from RealEstate r where r.createdDate >= ?1 and r.createdDate <= ?2 and r.status = ?3")
    public List<RealEstate> findByDateAndStatus(Date from, Date to, Status status);

    @Query("select r from RealEstate r where r.user.id = ?1")
    public Page<RealEstate> findByUser(Long userId, Pageable pageable);

    @Query("select r from RealEstate r where r.user.id = ?1 and r.status = ?2")
    public Page<RealEstate> findByUserAndStatus(Long userId, Status status, Pageable pageable);

    @Query("select r from RealEstate r where r.status = ?1")
    public Page<RealEstate> findByStatus(Status status, Pageable pageable);

    @Query("select count(r) from RealEstate r where r.createdDate = ?1")
    public Long soBaiDanghomNay(Date date);

    @Query("select count(r) from RealEstate r where r.user.id = ?1")
    public Long soBaiDangByUser(Long userId);

    @Query(value = "SELECT p.name, (SELECT COUNT(rm.id) from real_estate rm inner join wards wm on wm.id = rm.ward_id inner join districts dm on dm.id = wm.districts_id where dm.province_id = p.id) as soluong, p.id\n" +
            "from real_estate r\n" +
            "inner join wards w on w.id = r.ward_id\n" +
            "INNER JOIN districts d on d.id = w.districts_id\n" +
            "INNER JOIN province p on p.id = d.province_id group by p.id", nativeQuery = true)
    public List<Object[]> soLuongBdsCacTinh();

    @Query("select r from RealEstate r where r.status = ?1 order by r.id desc ")
    public Page<RealEstate> dsbdsTrangChu(Status status ,Pageable pageable);

    @Query("select r from RealEstate r where (r.price / r.acreage) >= ?1 and (r.price / r.acreage) <= ?2 and r.status = ?3 ")
    List<RealEstate> calSamePrice(Double min, Double max, Status status, Long id);

    @Query("select count(r.id) from RealEstate r where r.status = ?1")
    Long countViPham(Status status);
}
