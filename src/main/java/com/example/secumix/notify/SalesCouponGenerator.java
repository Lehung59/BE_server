package com.example.secumix.notify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SalesCouponGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesCouponGenerator.class);

    private static final int TEN_SECONDS = 10000;
    private static final int DELAY_FIVE_SECONDS = 5000;

    private final SalesStreamManager salesStreamManager;

    public SalesCouponGenerator(SalesStreamManager salesStreamManager) {
        this.salesStreamManager = salesStreamManager;
    }

    @Scheduled(fixedRate = TEN_SECONDS, initialDelay = DELAY_FIVE_SECONDS)
    public void generateCoupon() {
        String newSale = "randomSalesCouponGenerator.generateRandomCoupon()";

        LOGGER.info("Sending new sale notification. {}", newSale);

        salesStreamManager.notifySale(newSale);

    }

}
