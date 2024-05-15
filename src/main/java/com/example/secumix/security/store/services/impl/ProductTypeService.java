package com.example.secumix.security.store.services.impl;


import com.example.secumix.security.store.model.entities.ProductType;
import com.example.secumix.security.store.model.response.ProductTypeResponse;
import com.example.secumix.security.store.services.IProductTypeService;
import com.example.secumix.security.store.repository.ProductTypeRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductTypeService implements IProductTypeService {

    private ModelMapper modelMapper= new ModelMapper();
    @Autowired
    private ProductTypeRepo productTypeRepo;
    @Override
    public List<ProductTypeResponse> getAllProductType(int storeId) {
        return productTypeRepo.findByStoreId(storeId).stream()
                .map(productType -> {
                    ProductTypeResponse productTypeResponse= modelMapper.map(productType, ProductTypeResponse.class);
                    return productTypeResponse;
                }).collect(Collectors.toList());
    }

    @Override
    public Optional<ProductType> findProductTypeByName(String name, int storeId) {
        return productTypeRepo.findProductTypeByName(name, storeId);
    }

    @Override
    public void Save(ProductType productType) {
        productTypeRepo.save(productType);
    }
}
