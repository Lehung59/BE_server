package com.example.secumix.payload.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

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
    private Integer status;
    private String description;
    private String banReason;
    private Date updatedAt;
    private int view;
    private int storeId;
    private int productTypeId;
    private Set<ProductImageDto> productImages;
    private Set<OrderDetailDto> orderDetails;
    private Set<ImportDetailDto> importDetails;
}