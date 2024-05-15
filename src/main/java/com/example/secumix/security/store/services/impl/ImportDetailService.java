package com.example.secumix.security.store.services.impl;

import com.example.secumix.security.store.model.entities.ImportDetail;
import com.example.secumix.security.store.model.entities.Product;
import com.example.secumix.security.store.model.response.ImportResponse;
import com.example.secumix.security.store.model.response.ProductResponse;
import com.example.secumix.security.store.repository.ImportDetailRepo;
import com.example.secumix.security.store.repository.StoreRepo;
import com.example.secumix.security.store.services.IImportDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImportDetailService implements IImportDetailService {
    private final ImportDetailRepo importDetailRepo;
    private final StoreRepo storeRepo;

    @Override
    public Optional<ImportDetail> findByStore(int storeid) {
        return importDetailRepo.findById(storeid);
    }
    @Override
    public List<ImportDetail> findByStoreandProduct(int storeid, String productname) {
        return importDetailRepo.findByStoreandProduct(storeid,productname);
    }

    @Override
    public Page<ImportResponse> findAllImportPaginable(Pageable pageable, int storeid) {
        Page<ImportDetail> importDetails = importDetailRepo.getAllImportByStoreWithPagination(storeid,pageable);
        String storeName = storeRepo.findStoreById(storeid).get().getStoreName();
        List<ImportResponse> importResponses = importDetails
                .stream()
                .map(importDetail -> convertToImportResponse(importDetail, storeName))
                .collect(Collectors.toList());

        return new PageImpl<>(importResponses, pageable, importDetails.getTotalElements());
    }

    @Override
    public Page<ImportResponse> findImportByTitleContainingIgnoreCase(String keyword, Pageable pageable, int storeid) {
        Page<ImportDetail> importDetails = importDetailRepo.findImportByTitleContainingIgnoreCase(storeid, keyword, pageable);
        String storeName = storeRepo.findStoreById(storeid).get().getStoreName();
        List<ImportResponse> importResponses = importDetails
                .stream()
                .map(importDetail -> convertToImportResponse(importDetail, storeName))
                .collect(Collectors.toList());

        return new PageImpl<>(importResponses, pageable, importDetails.getTotalElements());
    }

    public static ImportResponse convertToImportResponse(ImportDetail importDetail, String storeName) {
        if (importDetail == null) {
            return null;
        }
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
