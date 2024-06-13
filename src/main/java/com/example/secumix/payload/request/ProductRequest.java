package com.example.secumix.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private int productId;
    private String productName;
    private String description;
    private long price;
    private int discount;
    private int quantity;
    private boolean status; // Optional, depending on whether you want the client to set this
    private String avatar; // Sử dụng MultipartFile để upload ảnh
    private int storeId; // Store ID if needed to associate the product
    private int productTypeId; // ProductType ID for associating with the product type

    // Constructor, getters and setters can be managed by Lombok annotations as shown
}