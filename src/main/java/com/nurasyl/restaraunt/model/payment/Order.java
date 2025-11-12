package com.nurasyl.restaraunt.model.payment;

import com.nurasyl.restaraunt.model.payment.cart.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class Order {
    private long id;
    private List<CartItem> items;
    private long total;
    private String status;
}
