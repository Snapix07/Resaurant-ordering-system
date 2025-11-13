package com.nurasyl.restaraunt.service;

import com.nurasyl.restaraunt.model.payment.cart.CartItem;
import com.nurasyl.restaraunt.types.DeliveryStrategy;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Getter
    private List<CartItem> items = new ArrayList<>();
    @Setter
    private DeliveryStrategy deliveryStrategy;

    public void addItem(CartItem item) {
        for (CartItem i : items) {
            if (i.getFoodId() != null && i.getFoodId().equals(item.getFoodId())) {
                i.setQuantity(i.getQuantity() + item.getQuantity());
                return;
            }
        }
        items.add(item);
    }

    public void removeItem(CartItem item) {
        items.removeIf(i -> {
            if (i.getFoodId() != null && i.getFoodId().equals(item.getFoodId())) {
                i.setQuantity(i.getQuantity() - item.getQuantity());
                return i.getQuantity() <= 0;
            }
            return false;
        });
    }

    public void clear() {
        items.clear();
    }

    public long calculateTotal() {
        long sum = 0;
        for (CartItem i : items) {
            sum += (long) (i.getQuantity() * i.getPrice());
        }
        if (deliveryStrategy != null) {
            sum += deliveryStrategy.calculateDeliveryFee();
        }
        return sum;
    }
}
