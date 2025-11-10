package com.nurasyl.restaraunt.model.delivery;

import com.nurasyl.restaraunt.types.OrderType;
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
