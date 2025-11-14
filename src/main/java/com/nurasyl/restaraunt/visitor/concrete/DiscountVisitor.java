package com.nurasyl.restaraunt.visitor.concrete;

import com.nurasyl.restaraunt.model.menu.food.*;
import com.nurasyl.restaraunt.visitor.MenuItemVisitor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class DiscountVisitor implements MenuItemVisitor {
    private int discountedPrice = 0;
    private final double pizzaDiscount = 0.10;
    private final double soupDiscount = 0.05;
    @Override
    public void visit(Pizza pizza) {
        discountedPrice = (int) (pizza.getPrice() - (pizza.getPrice() * pizzaDiscount));
    }

    @Override
    public void visit(Soup soup){
        discountedPrice = (int) (soup.getPrice() - (soup.getPrice() * soupDiscount));
    }

    @Override
    public void visit(Burger burger) {
        discountedPrice = burger.getPrice();
    }

    @Override
    public void visit(Coffee coffee) {
        discountedPrice = coffee.getPrice();
    }

    @Override
    public void visit(Salad salad) {
        discountedPrice = salad.getPrice();
    }

    @Override
    public void visit(Dessert dessert) {
        discountedPrice = dessert.getPrice();
    }

    @Override
    public void visit(Drink drink) {
        discountedPrice = drink.getPrice();
    }

    @Override
    public void visit(Pasta pasta) {
        discountedPrice = pasta.getPrice();
    }

    @Override
    public void visit(Snack snack) {
        discountedPrice = snack.getPrice();
    }
}
