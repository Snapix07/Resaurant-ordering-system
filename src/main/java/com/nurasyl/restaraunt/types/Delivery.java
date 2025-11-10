package com.nurasyl.restaraunt.types;

import com.nurasyl.restaraunt.factory.order.OrderType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Delivery implements OrderType {
    private String address;

    @Override
    public String getType() {
        return "Delivery";
    }

    @Override
    public void processOrder() {
        System.out.println("Delivery order to: " + address);
    }
}
