package com.example.secumix.notify;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/sales/{storeKey}/stream")
public class SalesController {

    private final SalesStreamManager salesStreamManager;

    public SalesController(SalesStreamManager salesStreamManager) {
        this.salesStreamManager = salesStreamManager;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> salesStream(@PathVariable int storeKey) {
        return salesStreamManager.listenSales(storeKey);
    }
}