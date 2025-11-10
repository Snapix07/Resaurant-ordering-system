package com.nurasyl.restaraunt.factory.order;

import com.nurasyl.restaraunt.types.Delivery;
import com.nurasyl.restaraunt.types.DineIn;
import com.nurasyl.restaraunt.types.Takeaway;
import org.springframework.stereotype.Component;

@Component
public class OrderFactory {
    public static OrderType createOrder(String type, String description) {
        switch (type) {
            case "Dine-In" -> {
                return new DineIn(description);
            }
            case "Takeaway" -> {
                return new Takeaway(description);
            }
            case "Delivery" -> {
                return new Delivery(description);
            }
            default -> throw new IllegalArgumentException("Invalid order type");
        }
    }
}
