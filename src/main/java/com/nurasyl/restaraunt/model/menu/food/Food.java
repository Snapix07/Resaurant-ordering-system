package com.nurasyl.restaraunt.model.menu.food;

import lombok.Data;

@Data
public class Food {
    private int id;
    private String name;
    private String category;
    private int price;
    private String description;
    private String image;
}
