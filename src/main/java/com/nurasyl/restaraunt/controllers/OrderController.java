package com.nurasyl.restaraunt.controllers;

import com.nurasyl.restaraunt.model.payment.Order;
import com.nurasyl.restaraunt.model.payment.cart.CartItem;
import com.nurasyl.restaraunt.service.CartService;
import com.nurasyl.restaraunt.service.OrderService;
import com.nurasyl.restaraunt.service.StripeService;
import com.nurasyl.restaraunt.types.DeliveryStrategy;
import com.nurasyl.restaraunt.model.payment.ProductRequest;
import com.nurasyl.restaraunt.model.payment.StripeResponse;

import com.nurasyl.restaraunt.utils.DineInStrategy;
import com.nurasyl.restaraunt.utils.HomeDeliveryStrategy;
import com.nurasyl.restaraunt.utils.TakeAwayStrategy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;
    private final StripeService stripeService;

    public OrderController(CartService cartService, OrderService orderService, StripeService stripeService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.stripeService = stripeService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> checkout(@RequestBody List<ProductRequest> products,
                                                   @RequestParam(required = false) String deliveryType) {

        DeliveryStrategy strategy = switch (deliveryType != null ? deliveryType : "TakeAway") {
            case "DineIn" -> new DineInStrategy();
            case "Delivery" -> new HomeDeliveryStrategy();
            default -> new TakeAwayStrategy();
        };


        List<CartItem> items = cartService.getItems();
        Order order = orderService.createOrder(items, strategy);

        long strategyPrice = strategy.calculateDeliveryFee();
        StripeResponse response = stripeService.checkoutProducts(products, strategyPrice);


        cartService.clear();



        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(order);
    }
}
