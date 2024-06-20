package com.example.secumix.notify;

import com.example.secumix.Utils.DtoMapper.NotifiMapper;
import com.example.secumix.payload.dtos.NotifiDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
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
    @Autowired
    private NotifiMapper notifiMapper;
    @Autowired
    private WebSocketController notificationController;

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

    public List<NotifiDto> getNotificationsByUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();String email = auth.getName();
        List<NotifiDto> notifies= notificationRepository.findNotifiesByEmail(email).stream().map(notifiMapper::toDto).toList();
        return notifies;
    }

    @Async
    public void notifyCustomer(Long customerId, String message) {
        notificationController.notifyClient(customerId, message);
    }

}
