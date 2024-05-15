package com.example.secumix.security.store.model.response;

import com.example.secumix.security.store.model.entities.OrderDetail;
import lombok.Data;

import java.util.List;

@Data
public class StoreCustomerRespone {
    private int customerId;
    private String customerName;
    private String customerPhoneNumber;
    private long totalPayment;
    private int totalOrder;

    private List<Integer> orderDetails;
}
