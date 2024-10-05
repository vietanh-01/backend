package com.web.repository;

import com.web.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select count(n.id) from Notification n where n.user.id = ?1 and n.isRead = false")
    public Long countNotificationNotIsReadByUser(Long userId);

    @Query(value = "select * from notification n where n.user_id = ?1 and n.is_read = false order by n.id desc limit 5", nativeQuery = true)
    public List<Notification> top5NotificationNotIsReadByUser(Long userId);

    @Query("select n from Notification n where n.user.id = ?1")
    public Page<Notification> notificationNotIsReadByUser(Long userId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Notification n set n.isRead = true where n.user.id = ?1")
    int markIsRead(Long id);
}
