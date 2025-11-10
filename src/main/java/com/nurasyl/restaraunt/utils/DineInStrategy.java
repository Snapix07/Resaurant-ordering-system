package com.nurasyl.restaraunt.utils;

import com.nurasyl.restaraunt.types.DeliveryStrategy;

public class DineInStrategy implements DeliveryStrategy {
    @Override
    public long calculateDeliveryFee() {
        return 0;
    }
}
