package com.nurasyl.restaraunt.utils;

import com.nurasyl.restaraunt.model.payment.Order;
import com.nurasyl.restaraunt.types.OrderObserver;
import org.springframework.stereotype.Component;

@Component
public class OrderLogger implements OrderObserver {

    @Override
    public void onOrderCreated(Order order) {
        System.out.println("Новый заказ создан: " + order.getId() + ", сумма: " + order.getTotal());
    }
}
