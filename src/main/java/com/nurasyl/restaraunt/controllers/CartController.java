package com.nurasyl.restaraunt.controllers;

import com.nurasyl.restaraunt.model.payment.cart.CartItem;
import com.nurasyl.restaraunt.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public List<CartItem> getCart() {
        return cartService.getItems();
    }

    @PostMapping("/add")
    public void addItem(@RequestBody CartItem item) {
        cartService.addItem(item);
    }

    @PostMapping("/remove")
    public void removeItem(@RequestBody CartItem item) {
        cartService.removeItem(item);
    }

    @PostMapping("/clear")
    public void clearCart() {
        cartService.clear();
    }

    @GetMapping("/total")
    public long getTotal() {
        return cartService.calculateTotal();
    }
}
