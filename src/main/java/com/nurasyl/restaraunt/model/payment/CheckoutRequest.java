package com.nurasyl.restaraunt.model.payment;

import java.util.List;

public class CheckoutRequest {
    private List<ProductRequest> products;
    private String deliveryType;

    public List<ProductRequest> getProducts() { return products; }
    public void setProducts(List<ProductRequest> products) { this.products = products; }

    public String getDeliveryType() { return deliveryType; }
    public void setDeliveryType(String deliveryType) { this.deliveryType = deliveryType; }
}
