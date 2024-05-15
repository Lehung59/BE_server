package com.example.secumix.security.store.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AddProductRequest {
    private String name;
    private String description;
    private int storeId;
    private String producttypename;
    private String avatar;
    private int price;

}
