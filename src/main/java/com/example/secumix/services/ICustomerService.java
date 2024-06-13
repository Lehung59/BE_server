package com.example.secumix.services;

import com.example.secumix.payload.response.StoreCustomerRespone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICustomerService {
    Page<StoreCustomerRespone> findAllCustomerPaginable(Pageable paging, int storeid);

    Page<StoreCustomerRespone> findCustomerByTitleContainingIgnoreCase(String keyword, Pageable paging, int storeid);
}
