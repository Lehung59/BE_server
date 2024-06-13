package com.example.secumix.payload.request;

import lombok.*;

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
