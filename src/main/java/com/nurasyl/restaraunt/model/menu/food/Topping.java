package com.nurasyl.restaraunt.model.menu.food;

import lombok.Data;

@Data
public class Topping {
    private int id;
    private String name;
    private String category;
    private int count;
    private int price;
}
