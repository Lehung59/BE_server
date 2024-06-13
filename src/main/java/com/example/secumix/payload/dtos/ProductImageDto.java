package com.example.secumix.payload.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class ProductImageDto {
    private int productImageId;
    private String imageProduct;
    private Date createdAt;
    private String title;
    private int status;
    private Date updatedAt;
    private int productId;
    private String productName;
}
