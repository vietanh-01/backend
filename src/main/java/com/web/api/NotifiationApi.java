package com.web.api;

import com.web.entity.Juridical;
import com.web.entity.Notification;
import com.web.service.JuridicalService;
import com.web.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@CrossOrigin
public class NotifiationApi {


    @Autowired
    private NotificationService notificationService;

    @GetMapping("/all/count-noti")
    public ResponseEntity<?> count(){
        Long result = notificationService.countNotificationNotIsReadByUser();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/all/top-noti")
    public ResponseEntity<?> getTop5(){
        List<Notification> result = notificationService.top5NotificationNotIsReadByUser();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/admin/find-all")
    public ResponseEntity<?> findAll(Pageable pageable){
        Page<Notification> result = notificationService.notificationNotIsReadByUser(pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/all/mark-read")
    public void markIsRead(){
        notificationService.markIsRead();
    }
}
