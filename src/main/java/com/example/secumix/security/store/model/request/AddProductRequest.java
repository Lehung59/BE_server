package com.example.secumix.security.store.model.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductRequest {
    private String name;
    private String description;
    private int storeId;
    private String producttypename;
    private String avatar;
    private long price;

}
