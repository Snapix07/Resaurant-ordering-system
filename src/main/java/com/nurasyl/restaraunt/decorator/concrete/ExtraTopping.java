package com.nurasyl.restaraunt.decorator.concrete;


import com.nurasyl.restaraunt.types.MenuItem;
import com.nurasyl.restaraunt.types.MenuItemDecorator;


public class ExtraTopping extends MenuItemDecorator {
    private String topping;
    private int toppingCount;
    private int toppingPrice;

    public ExtraTopping(MenuItem decoratedMenuItem, String topping, int toppingCount, int toppingPrice) {
        super(decoratedMenuItem);
        this.topping = topping;
        this.toppingCount = toppingCount;
        this.toppingPrice = toppingPrice;
    }


    @Override
    public String getDescription() {
        return decoratedMenuItem.getDescription() + ", +" + toppingCount + " " + topping;
    }

    @Override
    public int getPrice() {
        return decoratedMenuItem.getPrice() + (toppingPrice * toppingCount);
    }
}
