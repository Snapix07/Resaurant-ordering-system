package com.nurasyl.restaraunt.types;

import com.nurasyl.restaraunt.model.payment.Order;

public interface OrderObserver {
    void onOrderCreated(Order order);
}
