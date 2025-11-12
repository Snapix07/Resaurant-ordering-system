package com.nurasyl.restaraunt.config;

import com.nurasyl.restaraunt.service.OrderService;
import com.nurasyl.restaraunt.utils.OrderLogger;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class ObserverConfig {

    private final OrderService orderService;
    private final OrderLogger orderLogger;

    public ObserverConfig(OrderService orderService, OrderLogger orderLogger) {
        this.orderService = orderService;
        this.orderLogger = orderLogger;
    }

    @PostConstruct
    public void init() {
        orderService.addObserver(orderLogger);
    }
}
