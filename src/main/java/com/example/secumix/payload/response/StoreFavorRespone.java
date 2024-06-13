package com.example.secumix.payload.response;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class StoreFavorRespone {
    private int storeId;
    private String storeName;
    private String storeAddress;
    private String avatar;

}
