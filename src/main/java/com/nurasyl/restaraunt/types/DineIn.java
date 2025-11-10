package com.nurasyl.restaraunt.types;

import com.nurasyl.restaraunt.factory.order.OrderType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DineIn implements OrderType {
    private String tableNumber;

    @Override
    public String getType() {
        return "Dine-In";
    }

    @Override
    public void processOrder() {
        System.out.println("Order for table: " + tableNumber);
    }
}
