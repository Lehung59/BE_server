package com.example.secumix.security.store.services;


import com.example.secumix.security.store.model.entities.ProductType;
import com.example.secumix.security.store.model.response.ProductTypeResponse;

import java.util.List;
import java.util.Optional;

public interface IProductTypeService {
    List<ProductTypeResponse> getAllProductType(int storeId);
    Optional<ProductType> findProductTypeByName(String name, int storeId);
    void Save(ProductType productType);

}
