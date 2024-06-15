package com.example.secumix.payload.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDetailDto  {
    private int orderDetailId;
    private Date createdAt;
    private Date updatedAt;
    private int quantity;
    private String productName;
    private String storeName;
    private int storeId;
    private long priceTotal;
    private int productId;
    private Integer cartId;
    private int userId;
    private Integer shipperId;
    private int orderStatusId;
    private String customerPhone;
    private String customerName;
    private String address;
}
