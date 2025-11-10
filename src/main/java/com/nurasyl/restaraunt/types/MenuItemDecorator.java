package com.nurasyl.restaraunt.types;

import org.springframework.stereotype.Component;

@Component
public abstract class MenuItemDecorator implements MenuItem {
    protected MenuItem decoratedMenuItem;

    public MenuItemDecorator(MenuItem decoratedMenuItem) {
        this.decoratedMenuItem = decoratedMenuItem;
    }

    @Override
    public String getName(){
        return decoratedMenuItem.getName();
    }

    @Override
    public String getCategory(){
        return decoratedMenuItem.getDescription();
    }

    @Override
    public int getPrice(){
        return decoratedMenuItem.getPrice();
    }

    @Override
    public String getDescription(){
        return decoratedMenuItem.getDescription();
    }
}
