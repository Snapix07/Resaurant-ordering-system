package com.nurasyl.restaraunt.service;

import com.nurasyl.restaraunt.model.payment.Order;
import com.nurasyl.restaraunt.model.payment.cart.CartItem;
import com.nurasyl.restaraunt.types.DeliveryStrategy;

import com.nurasyl.restaraunt.types.OrderObserver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

    private final List<Order> orders = new ArrayList<>();
    private final AtomicLong orderCounter = new AtomicLong(1);

    private final List<OrderObserver> observers = new ArrayList<>();

    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(Order order) {
        for (OrderObserver observer : observers) {
            observer.onOrderCreated(order);
        }
    }

    public Order createOrder(List<CartItem> items, DeliveryStrategy strategy) {
        long orderId = orderCounter.getAndIncrement();
        long total = items.stream().mapToLong(i -> (long) (i.getQuantity() * i.getPrice())).sum();
        if (strategy != null) {
            total += strategy.calculateDeliveryFee();
        }

        Order order = new Order(orderId, items, total, "CREATED");
        orders.add(order);

        notifyObservers(order);
        return order;
    }

    public Order getOrderById(long id) {
        return orders.stream().filter(o -> o.getId() == id).findFirst().orElse(null);
    }
}
