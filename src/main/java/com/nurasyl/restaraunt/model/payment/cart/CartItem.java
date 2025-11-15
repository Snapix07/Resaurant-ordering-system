package com.nurasyl.restaraunt.model.payment.cart;


import com.nurasyl.restaraunt.model.menu.food.Topping;
import lombok.Data;

import java.util.List;


@Data
public class CartItem {
    private Long foodId;
    private String name;
    private String image;
    private int quantity;
    private Double price;
    private List<Topping> toppings;
}
