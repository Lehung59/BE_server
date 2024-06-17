package com.example.secumix.Utils.DtoMapper;

import com.example.secumix.payload.dtos.ImportDetailDto;
import com.example.secumix.entities.ImportDetail;
import com.example.secumix.entities.Product;
import com.example.secumix.payload.response.ImportResponse;
import com.example.secumix.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImportDetailMapper {
    @Autowired
    private ProductRepo productRepository;

    public ImportDetailDto toDto(ImportDetail importDetail) {
        if (importDetail == null) {
            return null;
        }

        ImportDetailDto importDetailDto = new ImportDetailDto();
        importDetailDto.setImportDetailId(importDetail.getImportDetailId());
        importDetailDto.setCreatedAt(importDetail.getCreatedAt());
        importDetailDto.setPrice(importDetail.getPrice());
        importDetailDto.setPriceTotal(importDetail.getPriceTotal());
        importDetailDto.setQuantity(importDetail.getQuantity());
        importDetailDto.setUpdatedAt(importDetail.getUpdatedAt());
        importDetailDto.setProductId(importDetail.getProduct() != null ? importDetail.getProduct().getProductId() : 0);
        importDetailDto.setProductName(importDetail.getProduct() != null ? importDetail.getProduct().getProductName() : null);

        return importDetailDto;
    }

    public ImportDetail toEntity(ImportDetailDto importDetailDto) {
        if (importDetailDto == null) {
            return null;
        }

        ImportDetail importDetail = new ImportDetail();
        importDetail.setImportDetailId(importDetailDto.getImportDetailId());
        importDetail.setCreatedAt(importDetailDto.getCreatedAt());
        importDetail.setPrice(importDetailDto.getPrice());
        importDetail.setPriceTotal(importDetailDto.getPriceTotal());
        importDetail.setQuantity(importDetailDto.getQuantity());
        importDetail.setUpdatedAt(importDetailDto.getUpdatedAt());

        if (importDetailDto.getProductId() > 0) {
            Product product = productRepository.findById(importDetailDto.getProductId()).orElse(null);
            importDetail.setProduct(product);
        }

        return importDetail;
    }

    public ImportResponse convertToImportResponse(ImportDetail importDetail) {
        if (importDetail == null) {
            return null;
        }
        String storeName = importDetail.getProduct().getStore().getStoreName();
        ImportResponse importResponse = new ImportResponse();
        importResponse.setImportDetailId(importDetail.getImportDetailId());
        importResponse.setCreatedAt(importDetail.getCreatedAt());
        importResponse.setPrice(importDetail.getPrice());
        importResponse.setPriceTotal(importDetail.getPriceTotal());
        importResponse.setQuantity(importDetail.getQuantity());
        importResponse.setUpdatedAt(importDetail.getUpdatedAt());
        // Set product name if the product is not null
        if (importDetail.getProduct() != null) {
            importResponse.setProductName(importDetail.getProduct().getProductName());
        }
        // Set store name
        importResponse.setStoreName(storeName);

        return importResponse;
    }
}
