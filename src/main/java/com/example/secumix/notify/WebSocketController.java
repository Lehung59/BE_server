package com.example.secumix.notify;

import com.example.secumix.ResponseObject;
import com.example.secumix.payload.dtos.NotifiDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class WebSocketController {

//    @GetMapping("/notifications")
//    ResponseEntity<ResponseObject> getNotifications() {
//        List<NotifiDto> notifications = notificationService.getNotificationsByUsername();
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("OK", "Thành công", notifications)
//        );
//    }

    @GetMapping("/notifications")
    public SseEmitter streamNotifications() {
        SseEmitter emitter = new SseEmitter();
//        List<NotifiDto> notifiDtos
        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    emitter.send(SseEmitter.event().name("message").data("Notification " + i));
                    Thread.sleep(1000);
                }
                emitter.complete();
            } catch (IOException | InterruptedException e) {
                emitter.completeWithError(e);
            }
        }).start();
        return emitter;
    }

    private final Map<Long, SseEmitter> clients = new ConcurrentHashMap<>();

    @GetMapping("/notifications/{clientId}")
    public SseEmitter streamNotifications(@PathVariable Long clientId) {
        SseEmitter emitter = new SseEmitter();
        clients.put(clientId, emitter);

        emitter.onCompletion(() -> clients.remove(clientId));
        emitter.onTimeout(() -> clients.remove(clientId));
        emitter.onError((e) -> clients.remove(clientId));

        return emitter;
    }

    public void notifyClient(Long clientId,  String message) {
        SseEmitter emitter = clients.get(clientId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("message").data(message));
            } catch (IOException e) {
                emitter.completeWithError(e);
                clients.remove(clientId);
            }
        }
    }
    @PostMapping("/api/v1/shipper/confirmDeli")
    public void confirmDelivery(@RequestParam Long customerId) {
        // Logic to confirm the delivery

        // Notify the customer
        this.notifyClient(customerId, "Your delivery has been successfully completed!");
    }


}
