package com.nurasyl.restaraunt.factory.menu;

import com.nurasyl.restaraunt.model.menu.food.*;
import com.nurasyl.restaraunt.types.MenuItem;
import org.springframework.stereotype.Component;

@Component
public class MenuFactory {
    public static MenuItem createMenu(String category, String name, int price, String description) {
        switch (category) {
            case "Pizza" -> {
                return new Pizza(name, price, description);
            }
            case "Burgers" -> {
                return new Burger(name, price, description);
            }
            case "Drinks" -> {
                return new Drink(name, price, description);
            }
            case "Salads" -> {
                return new Salad(name, price, description);
            }
            case "Coffee" -> {
                return new Coffee(name, price, description);
            }
            case "Desserts" -> {
                return new Dessert(name, price, description);
            }
            case "Pasta" -> {
                return new Pasta(name, price, description);
            }
            case "Snacks" -> {
                return new Snack(name, price, description);
            }
            case "Soups" -> {
                return new Soup(name, price, description);
            }
            default -> throw new IllegalArgumentException("Invalid menu category");
        }
    }
}
