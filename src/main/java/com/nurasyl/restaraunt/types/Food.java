package com.nurasyl.restaraunt.types;

import lombok.Data;

@Data
public class Food {
    private int id;
    private String name;
    private String category;
    private long price;
    private String description;
}
