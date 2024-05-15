package com.example.secumix.security.store.model.response;



import lombok.Data;

@Data
public class OrderDetailResponse {
    private int orderDetailId;
    private int quantity;
    private String productName;
    private int priceTotal;
    private String orderStatusName;
    private String address;
    private String productImg;
    private String storeName;
}
