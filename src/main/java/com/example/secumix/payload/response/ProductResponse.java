package com.example.secumix.payload.response;



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
    private int storeId;
    private int productTypeId;
    private String productType;

}
