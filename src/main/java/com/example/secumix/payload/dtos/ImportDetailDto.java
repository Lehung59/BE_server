package com.example.secumix.payload.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class ImportDetailDto {
    private int importDetailId;
    private Date createdAt;
    private long price;
    private long priceTotal;
    private int quantity;
    private Date updatedAt;
    private int productId;
    private String productName;

}
