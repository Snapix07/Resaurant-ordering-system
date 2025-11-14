package com.nurasyl.restaraunt.service;

import com.nurasyl.restaraunt.model.payment.ProductRequest;
import com.nurasyl.restaraunt.model.payment.StripeResponse;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StripeService {
    @Value("${stripe.secretKey}")
    private String apiKey;

    public StripeResponse checkoutProducts(List<ProductRequest> products, long strategyPrice) {
        Stripe.apiKey = apiKey;
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for (ProductRequest product : products) {
            if (product.getName() == null || product.getAmount() == null || product.getQuantity() == null) {
                throw new IllegalArgumentException("Invalid product data: " + product);
            }
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(product.getName())
                            .build();

            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("KZT")
                            .setUnitAmount(product.getAmount() * 100)
                            .setProductData(productData)
                            .build();

            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams.LineItem.builder()
                            .setPriceData(priceData)
                            .setQuantity((long) product.getQuantity())
                            .build();

            lineItems.add(lineItem);
        }


        if (strategyPrice > 0) {
            SessionCreateParams.LineItem.PriceData.ProductData deliveryData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName("Delivery")
                            .build();

            SessionCreateParams.LineItem.PriceData deliveryPriceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("KZT")
                            .setUnitAmount(strategyPrice * 100)
                            .setProductData(deliveryData)
                            .build();

            SessionCreateParams.LineItem deliveryLineItem =
                    SessionCreateParams.LineItem.builder()
                            .setPriceData(deliveryPriceData)
                            .setQuantity(1L)
                            .build();

            lineItems.add(deliveryLineItem);
        }


        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/success")
                .setCancelUrl("http://localhost:8080/cancel")
                .addAllLineItem(lineItems)
                .setShippingAddressCollection(
                        SessionCreateParams.ShippingAddressCollection.builder()
                                .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.KZ)
                                .build()
                )
                .build();

        try {
            Session session = Session.create(params);
            return StripeResponse.builder()
                    .status("Success")
                    .message("Payment session created successfully")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Stripe session creation failed", e);
        }
    }
}
