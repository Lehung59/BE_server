package com.example.secumix.Utils.DtoMapper;

import com.example.secumix.payload.dtos.ProductImageDto;
import com.example.secumix.entities.Product;
import com.example.secumix.entities.ProductImage;
import com.example.secumix.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductImageMapper {
    @Autowired
    private ProductRepo productRepository;

    public ProductImageDto toDto(ProductImage productImage) {
        if (productImage == null) {
            return null;
        }

        ProductImageDto productImageDto = new ProductImageDto();
        productImageDto.setProductImageId(productImage.getProductImageId());
        productImageDto.setImageProduct(productImage.getImageProduct());
        productImageDto.setCreatedAt(productImage.getCreatedAt());
        productImageDto.setTitle(productImage.getTitle());
        productImageDto.setStatus(productImage.getStatus());
        productImageDto.setUpdatedAt(productImage.getUpdatedAt());
        productImageDto.setProductId(productImage.getProduct() != null ? productImage.getProduct().getProductId() : 0);
        productImageDto.setProductName(productImage.getProduct() != null ? productImage.getProduct().getProductName() : null);

        return productImageDto;
    }

    public ProductImage toEntity(ProductImageDto productImageDto) {
        if (productImageDto == null) {
            return null;
        }

        ProductImage productImage = new ProductImage();
        productImage.setProductImageId(productImageDto.getProductImageId());
        productImage.setImageProduct(productImageDto.getImageProduct());
        productImage.setCreatedAt(productImageDto.getCreatedAt());
        productImage.setTitle(productImageDto.getTitle());
        productImage.setStatus(productImageDto.getStatus());
        productImage.setUpdatedAt(productImageDto.getUpdatedAt());

        if (productImageDto.getProductId() > 0) {
            Product product = productRepository.findById(productImageDto.getProductId()).orElse(null);
            productImage.setProduct(product);
        }

        return productImage;
    }
}
