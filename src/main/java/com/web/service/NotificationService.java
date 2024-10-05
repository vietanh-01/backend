package com.web.service;

import com.web.entity.Notification;
import com.web.entity.User;
import com.web.repository.NotificationRepository;
import com.web.repository.UserRepository;
import com.web.utils.Contains;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    public void save(String title, String link, String message){
        List<User> users = userRepository.getUserByRole(Contains.ROLE_ADMIN);
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("content", message);
        map.put("link", link);
        for(User u : users){
            Notification notification = new Notification();
            notification.setCreatedDate(LocalDateTime.now());
            notification.setIsRead(false);
            notification.setLink(link);
            notification.setTitle(title);
            notification.setUser(u);
            notificationRepository.save(notification);
            simpMessagingTemplate.convertAndSendToUser(u.getEmail(), "/queue/notification", message, map);
        }
    }

    public List<Notification> top5NotificationNotIsReadByUser(){
        User user = userUtils.getUserWithAuthority();
        List<Notification> list = notificationRepository.top5NotificationNotIsReadByUser(user.getId());
        return list;
    }

    public Long countNotificationNotIsReadByUser(){
        User user = userUtils.getUserWithAuthority();
        Long result = notificationRepository.countNotificationNotIsReadByUser(user.getId());
        return result;
    }

    public Page<Notification> notificationNotIsReadByUser(Pageable pageable){
        User user = userUtils.getUserWithAuthority();
        Page<Notification> page = notificationRepository.notificationNotIsReadByUser(user.getId(), pageable);
        return page;
    }

    public void markIsRead(){
        User user = userUtils.getUserWithAuthority();
        notificationRepository.markIsRead(user.getId());
    }
}
