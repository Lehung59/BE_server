package com.example.secumix.payload.response;


import lombok.Data;

import java.util.Date;
@Data
public class ImportResponse {
    private int importDetailId;
    private Date createdAt;
    private long price;
    private long priceTotal;
    private int quantity;
    private Date updatedAt;
    private String productName;
    private String storeName;
}
