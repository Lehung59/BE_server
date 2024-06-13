package com.example.secumix.services;


import com.example.secumix.entities.ProductType;
import com.example.secumix.payload.response.ProductTypeResponse;

import java.util.List;
import java.util.Optional;

public interface IProductTypeService {
    List<ProductTypeResponse> getAllProductType(int storeId);
    Optional<ProductType> findProductTypeByName(String name, int storeId);
    void Save(ProductType productType);

}
