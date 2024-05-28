package com.example.secumix.security.store.model.dtos;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class StoreDto {
    private int storeId;
    private String storeName;
    private String address;
    private String phoneNumber;
    private int rate;
    private String image;
    private int storeTypeId;
    private List<String> productType;
    private List<String> productList;
    private String emailManager;
    private Set<String> userEmail;
}