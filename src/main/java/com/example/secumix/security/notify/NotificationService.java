package com.example.secumix.security.notify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotifyRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Notify addNotification(Notify notification) {
        Notify savedNotification = notificationRepository.save(notification);
        sendNotificationToUser(savedNotification);
        return savedNotification;
    }

    private void sendNotificationToUser(Notify notification) {
        messagingTemplate.convertAndSendToUser(
                notification.getUser().getUsername(), // Định danh người dùng làm điểm đến
                "/queue/notifications",
                notification);
    }

    public List<Notify> getNotificationsByUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        List<Notify> notifies= notificationRepository.findNotifiesByEmail(email);
        return notifies;
    }
}
