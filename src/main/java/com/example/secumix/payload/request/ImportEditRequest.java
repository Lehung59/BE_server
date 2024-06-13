package com.example.secumix.payload.request;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImportEditRequest {
    private int importDetailId;
    private long price;
    private long priceTotal;
    private int quantity;
}
