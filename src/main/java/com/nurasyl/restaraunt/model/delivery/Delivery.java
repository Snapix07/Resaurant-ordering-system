package com.nurasyl.restaraunt.model.delivery;

import com.nurasyl.restaraunt.types.OrderType;
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