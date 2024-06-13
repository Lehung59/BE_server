package com.example.secumix.payload.response;

import lombok.Data;

@Data
public class CartItemResponse {
    private int cartItemId;

    private int quantity;
    private String productName;
    private long priceTotal;
    private String productImg;
    private String storeName;

}
