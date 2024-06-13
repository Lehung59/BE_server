package com.example.secumix.payload.request;


import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CartItemRequest {
    private int quantity;
    private int productid;

    public CartItemRequest(int quantity, int productid) {
        this.quantity = quantity;
        this.productid = productid;
    }
}
