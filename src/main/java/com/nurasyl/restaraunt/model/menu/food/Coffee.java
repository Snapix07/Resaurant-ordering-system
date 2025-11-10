package com.nurasyl.restaraunt.model.menu.food;

import com.nurasyl.restaraunt.types.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Coffee implements MenuItem {
    private String name;
    private int price;
    private String description;

    @Override
    public String getCategory() {
        return "Coffee";
    }
}
