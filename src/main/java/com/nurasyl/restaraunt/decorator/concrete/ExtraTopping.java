package com.nurasyl.restaraunt.decorator.concrete;

<<<<<<< HEAD

import com.nurasyl.restaraunt.types.MenuItem;
import com.nurasyl.restaraunt.types.MenuItemDecorator;
=======
import com.nurasyl.restaraunt.decorator.MenuItemDecorator;
import com.nurasyl.restaraunt.factory.menu.MenuItem;
>>>>>>> 6113df0cea5b0f39c0be89b33784e1e6115e7a54

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
