package com.nurasyl.restaraunt.factory.order;

<<<<<<< HEAD
import com.nurasyl.restaraunt.model.delivery.*;
import com.nurasyl.restaraunt.types.OrderType;
=======
import com.nurasyl.restaraunt.types.Delivery;
import com.nurasyl.restaraunt.types.DineIn;
import com.nurasyl.restaraunt.types.Takeaway;
>>>>>>> 6113df0cea5b0f39c0be89b33784e1e6115e7a54
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
