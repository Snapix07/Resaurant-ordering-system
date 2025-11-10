package com.nurasyl.restaraunt.decorator.concrete;


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
