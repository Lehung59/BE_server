package com.example.secumix.security.store.model.response;


import lombok.Data;

import java.util.Date;
@Data
public class ImportResponse {
    private int importDetailId;
    private Date createdAt;
    private int price;
    private int priceTotal;
    private int quantity;
    private Date updatedAt;
    private String productName;
    private String storeName;
}
