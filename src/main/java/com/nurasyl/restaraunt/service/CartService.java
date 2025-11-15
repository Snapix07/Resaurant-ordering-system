package com.nurasyl.restaraunt.service;

import com.nurasyl.restaraunt.model.menu.food.Topping;
import com.nurasyl.restaraunt.model.payment.cart.CartItem;
import com.nurasyl.restaraunt.types.DeliveryStrategy;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CartService {
    @Getter
    private List<CartItem> items = new ArrayList<>();
    @Setter
    private DeliveryStrategy deliveryStrategy;

    private long nextId = 1;
    public void addItem(CartItem item) {
        if (item.getFoodId() == null) {
            item.setFoodId(nextId++);
        }
        for (CartItem i : items) {
            if (i.getFoodId() != null && i.getFoodId().equals(item.getFoodId())) {
                if(toppingsMatch(i.getToppings(), item.getToppings())) {
                    i.setQuantity(i.getQuantity() + item.getQuantity());
                    return;
                }
            }
        }
        items.add(item);
    }

    public void removeItem(CartItem item) {
        items.removeIf(i -> {
            if (i.getFoodId() != null && i.getFoodId().equals(item.getFoodId())) {
                if(toppingsMatch(i.getToppings(), item.getToppings())) {
                    i.setQuantity(i.getQuantity() - item.getQuantity());
                    return i.getQuantity() <= 0;
                }
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

    private boolean toppingsMatch(List<Topping> toppings1, List<Topping> toppings2) {
        if ((toppings1 == null || toppings1.isEmpty()) &&
                (toppings2 == null || toppings2.isEmpty())) {
            return true;
        }

        if ((toppings1 == null || toppings1.isEmpty()) ||
                (toppings2 == null || toppings2.isEmpty())) {
            return false;
        }
        if (toppings1.size() != toppings2.size()) {
            return false;
        }

        Set<Integer> ids1 = new HashSet<>();
        Set<Integer> ids2 = new HashSet<>();

        for (Topping t : toppings1) {
            ids1.add(t.getId());
        }

        for (Topping t : toppings2) {
            ids2.add(t.getId());
        }

        return ids1.equals(ids2);
    }
}
