package com.example.secumix.security.store.model.request;

import com.example.secumix.security.store.model.dtos.CustomerDto;
import com.example.secumix.security.store.model.dtos.ProductDto;
import com.example.secumix.security.store.model.entities.Product;
import com.example.secumix.security.store.model.entities.ProductType;
import com.example.secumix.security.store.model.entities.StoreType;
import com.example.secumix.security.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
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
