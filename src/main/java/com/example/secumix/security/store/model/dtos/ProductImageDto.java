package com.example.secumix.security.store.model.dtos;

import com.example.secumix.security.store.model.entities.Product;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Data
public class ProductImageDto {
    private int productImageId;
    private String imageProduct;
    private Date createdAt;
    private String title;
    private int status;
    private Date updatedAt;
    private int productId;
    private String productName;
}
