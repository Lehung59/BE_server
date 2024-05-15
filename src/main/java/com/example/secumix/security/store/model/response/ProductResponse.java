package com.example.secumix.security.store.model.response;



import lombok.Data;


@Data
public class ProductResponse {
    private int productId;
    private String avatarProduct;
    private int discount;
    private int price;
    private String productName;
    private int quantity;
    private int status;


    private String description;


    private int view;


    private String storeName;


    private String productType;

}
