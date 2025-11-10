package com.nurasyl.restaraunt.decorator.concrete;

import com.nurasyl.restaraunt.decorator.MenuItemDecorator;
import com.nurasyl.restaraunt.factory.menu.MenuItem;

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
