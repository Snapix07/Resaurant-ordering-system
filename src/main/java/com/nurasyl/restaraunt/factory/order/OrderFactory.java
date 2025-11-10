package com.nurasyl.restaraunt.factory.order;

import com.nurasyl.restaraunt.model.delivery.Delivery;
import com.nurasyl.restaraunt.model.delivery.DineIn;
import com.nurasyl.restaraunt.model.delivery.Takeaway;
import com.nurasyl.restaraunt.types.OrderType;
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
