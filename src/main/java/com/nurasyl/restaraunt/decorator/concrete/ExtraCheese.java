package com.nurasyl.restaraunt.decorator.concrete;

<<<<<<< HEAD

import com.nurasyl.restaraunt.types.MenuItem;
import com.nurasyl.restaraunt.types.MenuItemDecorator;
=======
import com.nurasyl.restaraunt.decorator.MenuItemDecorator;
import com.nurasyl.restaraunt.factory.menu.MenuItem;
>>>>>>> 6113df0cea5b0f39c0be89b33784e1e6115e7a54

public class ExtraCheese extends MenuItemDecorator {

    public ExtraCheese(MenuItem decoratedMenuItem) {
        super(decoratedMenuItem);
    }

    @Override
    public String getDescription() {
        return decoratedMenuItem.getDescription() + ", ExtraCheese";
    }

    @Override
    public int getPrice() {
        return decoratedMenuItem.getPrice() + 100;
    }
}
