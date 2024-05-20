package com.example.secumix.security.store.model.request;

import lombok.*;

@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class StoreInfoEditRequest {
    private int storeId;
    private String storeName;
    private String address;
    private String phonumber;
    private String avatar;


}
