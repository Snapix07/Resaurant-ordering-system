package com.nurasyl.restaraunt.model.payment.cart;


import lombok.Data;


@Data
public class CartItem {
    private Long foodId;
    private int quantity;
    private Double price;
}
