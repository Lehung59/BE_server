package com.example.secumix.payload.dtos;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CustomerDto {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private boolean enabled;
    private boolean onlineStatus;
    private List<OrderDetailDto> orderDetails;
    private Set<StoreDto> stores;

}
