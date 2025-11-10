package com.nurasyl.restaraunt.types;

import com.nurasyl.restaraunt.factory.order.OrderType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Takeaway implements OrderType {
    private String pickupTime;

    @Override
    public String getType() {
        return "Takeaway";
    }

    @Override
    public void processOrder() {
        System.out.println("Takeaway order at: " + pickupTime);
    }
}
