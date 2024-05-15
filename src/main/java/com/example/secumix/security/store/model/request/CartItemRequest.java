package com.example.secumix.security.store.model.request;


import lombok.Data;

@Data
public class CartItemRequest {
    private int quantity;
    private int productid;

    public CartItemRequest(int quantity, int productid) {
        this.quantity = quantity;
        this.productid = productid;
    }
}
