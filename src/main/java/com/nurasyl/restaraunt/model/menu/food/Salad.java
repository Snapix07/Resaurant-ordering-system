package com.nurasyl.restaraunt.model.menu.food;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Salad implements MenuItem {
    private String name;
    private int price;
    private String description;

    @Override
    public String getCategory() {
        return "Salads";
    }
}
