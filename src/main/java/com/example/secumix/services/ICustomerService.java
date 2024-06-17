package com.example.secumix.services;

import com.example.secumix.entities.ProfileDetail;
import com.example.secumix.payload.response.StoreCustomerRespone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICustomerService {
    Page<ProfileDetail> findAllCustomerPaginable(int storeid, int page, int size);

    Page<ProfileDetail> findCustomerByTitleContainingIgnoreCase(String keyword,int page, int size,  int storeid);

}
