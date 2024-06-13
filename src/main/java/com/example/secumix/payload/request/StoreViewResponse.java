package com.example.secumix.payload.request;

import com.example.secumix.payload.dtos.CustomerDto;
import com.example.secumix.payload.dtos.ProductDto;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreViewResponse {

    private int storeId;
    private String storeName;
    private String address;
    private String phoneNumber;
    private int rate;
    private String image;
    private String storeType;
    private List<String> productType;
    private List<ProductDto> productList;
    private String emailmanager;
    private Set<CustomerDto> customerDtos = new HashSet<>();
}
