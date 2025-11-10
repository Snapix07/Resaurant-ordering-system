package com.nurasyl.restaraunt.utils;

import com.nurasyl.restaraunt.types.DeliveryStrategy;

public class HomeDeliveryStrategy implements DeliveryStrategy {
    @Override
    public long calculateDeliveryFee() {
        return 1000;
    }
}
