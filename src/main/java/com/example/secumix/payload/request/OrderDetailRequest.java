package com.example.secumix.payload.request;

import lombok.Data;

@Data
public class OrderDetailRequest {
    private int quantity;
    private int productid;

    public OrderDetailRequest(int quantity, int productid) {
        this.quantity = quantity;
        this.productid = productid;
    }
}
