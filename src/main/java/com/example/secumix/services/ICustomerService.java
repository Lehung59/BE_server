package com.example.secumix.services;

import com.example.secumix.payload.response.StoreCustomerRespone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICustomerService {
    List<StoreCustomerRespone> findAllCustomerPaginable(int storeid);

    List<StoreCustomerRespone> findCustomerByTitleContainingIgnoreCase(String keyword,  int storeid);
}
