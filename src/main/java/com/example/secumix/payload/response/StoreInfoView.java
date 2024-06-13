package com.example.secumix.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StoreInfoView {
    private int storeId;
    private String storeName;
    private String address;
    private String phoneNumber;
    private int rate;
    private String image;
    private List<String> productTypeName;
    private List<String> productListName;
    private String emailmanager;


}
