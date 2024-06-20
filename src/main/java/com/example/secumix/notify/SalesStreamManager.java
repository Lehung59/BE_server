package com.example.secumix.notify;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

import java.time.Duration;

@Service
public class SalesStreamManager {

    private final FluxProcessor<String, String> processor;
    private final FluxSink<String> sink;

    public SalesStreamManager() {
        processor = DirectProcessor.<String>create().serialize();
        sink = processor.sink();
    }

    void notifySale(final String coupon) {
        sink.next(coupon);
    }

    Flux<ServerSentEvent<String>> listenSales(int storeKey) {
        return processor
                .map(c -> ServerSentEvent
                        .builder(c)
                        .event("sales-event")
                        .retry(Duration.ofSeconds(20))
                        .build());
    }
}
