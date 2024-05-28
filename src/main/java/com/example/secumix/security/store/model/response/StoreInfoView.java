package com.example.secumix.security.store.model.response;

import com.example.secumix.security.store.model.entities.Product;
import com.example.secumix.security.store.model.entities.ProductType;
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
