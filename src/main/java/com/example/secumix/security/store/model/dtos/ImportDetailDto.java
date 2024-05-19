package com.example.secumix.security.store.model.dtos;

import com.example.secumix.security.store.model.entities.Product;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Data
public class ImportDetailDto {
    private int importDetailId;
    private Date createdAt;
    private long price;
    private long priceTotal;
    private int quantity;
    private Date updatedAt;
    private int productId;
    private String productName;

}
