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
            long finalAmount = (product.getAmount() + strategyPrice) * product.getQuantity() * 100;

            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(product.getName())
                            .build();

            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("KZT")
                            .setUnitAmount(finalAmount)
                            .setProductData(productData)
                            .build();

            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams.LineItem.builder()
                            .setPriceData(priceData)
                            .setQuantity(1L)
                            .build();

            lineItems.add(lineItem);
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



        Session session = null;
        try{
            session = Session.create(params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return StripeResponse.builder()
                .status("Success")
                .message("Payment section created successfully")
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .build();
    }
}
