package com.example.secumix.security.store.model.dtos;

import com.example.secumix.security.store.model.entities.Product;
import com.example.secumix.security.store.repository.StoreRepo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Convert;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
@Data
@Getter
@Setter
public class ProductDto {
    private int productId;
    private String avatarProduct;
    private Date createdAt;
    private int discount;
    private long price;
    private String productName;
    private int quantity;
    private int status;
    private String description;
    private Date updatedAt;
    private int view;
    private int storeId;
    private int productTypeId;
    private Set<ProductImageDto> productImages;
    private Set<OrderDetailDto> orderDetails;
    private Set<ImportDetailDto> importDetails;
}