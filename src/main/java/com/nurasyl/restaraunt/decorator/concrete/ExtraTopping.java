package com.nurasyl.restaraunt.decorator.concrete;


import com.nurasyl.restaraunt.types.MenuItem;
import com.nurasyl.restaraunt.types.MenuItemDecorator;


public class ExtraTopping extends MenuItemDecorator {
    private String topping;
    private int toppingPrice;

    public ExtraTopping(MenuItem decoratedMenuItem, String topping, int toppingPrice) {
        super(decoratedMenuItem);
        this.topping = topping;
        this.toppingPrice = toppingPrice;
    }


    @Override
    public String getDescription() {
        return decoratedMenuItem.getDescription() + ", + " + topping;
    }

    @Override
    public int getPrice() {
        return decoratedMenuItem.getPrice() + toppingPrice;
    }
}
