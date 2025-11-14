package com.nurasyl.restaraunt.visitor;

import com.nurasyl.restaraunt.model.menu.food.*;

public interface MenuItemVisitor {
    void visit(Pizza pizza);
    void visit(Burger burger);
    void visit(Coffee coffee);
    void visit(Dessert dessert);
    void visit(Drink drink);
    void visit(Pasta pasta);
    void visit(Salad salad);
    void visit(Snack snack);
    void visit(Soup soup);
}
