package com.nurasyl.restaraunt.utils;

import com.nurasyl.restaraunt.types.DeliveryStrategy;

public class TakeAwayStrategy implements DeliveryStrategy {
    @Override
    public long calculateDeliveryFee() {
        return 200;
    }
}