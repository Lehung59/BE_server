package com.example.secumix.security.Utils.DtoMapper;

import com.example.secumix.security.store.model.dtos.ProductDto;
import com.example.secumix.security.store.model.entities.Product;
import com.example.secumix.security.store.model.entities.ProductType;
import com.example.secumix.security.store.model.entities.Store;
import com.example.secumix.security.store.repository.ProductRepo;
import com.example.secumix.security.store.repository.ProductTypeRepo;
import com.example.secumix.security.store.repository.StoreRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final StoreRepo storeRepo;

    private final ProductTypeRepo productTypeRepo;
    private final ProductImageMapper productImageMapper;
    private final ImportDetailMapper importDetailMapper;
    private final OrderDetailMapper orderDetailMapper;

    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }

        ProductDto productDto = new ProductDto();
        productDto.setProductId(product.getProductId());
        productDto.setAvatarProduct(product.getAvatarProduct());
        productDto.setCreatedAt(product.getCreatedAt());
        productDto.setDiscount(product.getDiscount());
        productDto.setPrice(product.getPrice());
        productDto.setProductName(product.getProductName());
        productDto.setQuantity(product.getQuantity());
        productDto.setStatus(product.getStatus());
        productDto.setDescription(product.getDescription());
        productDto.setUpdatedAt(product.getUpdatedAt());
        productDto.setView(product.getView());
        productDto.setStoreId(product.getStore() != null ? product.getStore().getStoreId() : 0);
        productDto.setProductTypeId(product.getProductType() != null ? product.getProductType().getProductTypeId() : 0);
        // Convert nested objects if necessary
        productDto.setProductImages(product.getProductImages().stream().map(productImageMapper::toDto).collect(Collectors.toSet()));
        productDto.setOrderDetails(product.getOrderDetails().stream().map(orderDetailMapper::toDto).collect(Collectors.toSet()));
        productDto.setImportDetails(product.getImportDetails().stream().map(importDetailMapper::toDto).collect(Collectors.toSet()));

        return productDto;
    }

    public Product toEntity(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }

        Product product = new Product();
        product.setProductId(productDto.getProductId());
        product.setAvatarProduct(productDto.getAvatarProduct());
        product.setCreatedAt(productDto.getCreatedAt());
        product.setDiscount(productDto.getDiscount());
        product.setPrice(productDto.getPrice());
        product.setProductName(productDto.getProductName());
        product.setQuantity(productDto.getQuantity());
        product.setStatus(productDto.getStatus());
        product.setDescription(productDto.getDescription());
        product.setUpdatedAt(productDto.getUpdatedAt());
        product.setView(productDto.getView());

        // Fetch and set the store entity if the storeId is provided
        if (productDto.getStoreId() > 0) {
            Store store = storeRepo.findById(productDto.getStoreId()).orElse(null);
            product.setStore(store);
        }

        // Fetch and set the product type entity if the productTypeId is provided
        if (productDto.getProductTypeId() > 0) {
            ProductType productType = productTypeRepo.findById(productDto.getProductTypeId()).orElse(null);
            product.setProductType(productType);
        }

        // Convert nested objects if necessary
        // product.setProductImages(productDto.getProductImages().stream().map(ProductImageDto::toProductImage).collect(Collectors.toSet()));
        // product.setOrderDetails(productDto.getOrderDetails().stream().map(OrderDetailDto::toOrderDetail).collect(Collectors.toSet()));
        // product.setImportDetails(productDto.getImportDetails().stream().map(ImportDetailDto::toImportDetail).collect(Collectors.toSet()));

        return product;
    }
}
