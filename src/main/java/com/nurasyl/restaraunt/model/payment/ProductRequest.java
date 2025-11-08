package com.nurasyl.restaraunt.model.payment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private Long amount;
    private String currency;
    private Long quantity;
    private String name;
}
