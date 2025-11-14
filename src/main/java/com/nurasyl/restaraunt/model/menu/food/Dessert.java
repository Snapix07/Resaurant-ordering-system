package com.nurasyl.restaraunt.model.menu.food;


import com.nurasyl.restaraunt.types.MenuItem;

import com.nurasyl.restaraunt.types.Visit;
import com.nurasyl.restaraunt.visitor.MenuItemVisitor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Dessert implements MenuItem, Visit {
    private String name;
    private int price;
    private String description;

    @Override
    public String getCategory() {
        return "Desserts";
    }

    @Override
    public void accept(MenuItemVisitor visitor){
        visitor.visit(this);
    }
}
