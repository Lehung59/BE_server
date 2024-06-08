package com.example.secumix.security.notify;

import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.store.model.dtos.NotifiDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WebSocketController {
    @Autowired
    private NotificationService notificationService;
    @GetMapping("/notifications")
    ResponseEntity<ResponseObject> getNotifications() {
        List<NotifiDto> notifications = notificationService.getNotificationsByUsername();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Thành công", notifications)
        );
    }
}
