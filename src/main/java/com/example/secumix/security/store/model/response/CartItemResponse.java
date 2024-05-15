package com.example.secumix.security.store.model.response;

import lombok.Data;

@Data
public class CartItemResponse {
    private int cartItemId;

    private int quantity;
    private String productName;
    private int priceTotal;
    private String productImg;
    private String storeName;

}
