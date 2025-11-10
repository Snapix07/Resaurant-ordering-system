package com.nurasyl.restaraunt.model.menu.food;

import com.nurasyl.restaraunt.factory.menu.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Snack implements MenuItem {
    private String name;
    private int price;
    private String description;

    @Override
    public String getCategory() {
        return "Snacks";
    }
}
