package com.example.secumix.security.store.model.response;



import lombok.Builder;
import lombok.Data;


@Data
public class ProductResponse {
    private int productId;
    private String avatarProduct;
    private int discount;
    private long price;
    private String productName;
    private int quantity;
    private boolean status;


    private String description;


    private int view;


    private String storeName;


    private String productType;

}
