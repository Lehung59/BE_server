package com.example.secumix.services.impl;

import com.example.secumix.exception.CustomException;
import com.example.secumix.Utils.UserUtils;
import com.example.secumix.entities.ImportDetail;
import com.example.secumix.payload.request.ImportEditRequest;
import com.example.secumix.payload.response.ImportResponse;
import com.example.secumix.repository.ImportDetailRepo;
import com.example.secumix.repository.StoreRepo;
import com.example.secumix.services.IImportDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImportDetailService implements IImportDetailService {
    private final ImportDetailRepo importDetailRepo;
    private final StoreRepo storeRepo;
    private final UserUtils userUtils;

    @Override
    public Optional<ImportDetail> findByStore(int storeid) {
        return importDetailRepo.findById(storeid);
    }
    @Override
    public List<ImportDetail> findByStoreandProduct(int storeid, String productname) {
        return importDetailRepo.findByStoreandProduct(storeid,productname);
    }

    @Override
    public Page<ImportDetail> findAllImportPaginable(int storeId, int page, int size) {
        Pageable paging = PageRequest.of(page - 1, size);
        storeId = userUtils.getStoreId();
        return importDetailRepo.getAllImportByStoreWithPagination(storeId, paging);
    }

    @Override
    public Page<ImportDetail> findImportByTitleContainingIgnoreCase(String keyword, int storeId, int page, int size) {
        Pageable paging = PageRequest.of(page - 1, size);
        storeId = userUtils.getStoreId();
        return importDetailRepo.findImportByTitleContainingIgnoreCase(storeId, paging, keyword);
    }

    @Override
    public ImportResponse updateImport(ImportEditRequest importEditRequest) throws CustomException {
        ImportDetail importDetail = importDetailRepo.findById(importEditRequest.getImportDetailId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "ImportDetail not found"));
        List<ImportDetail> importDetailList = importDetailRepo.findByStore( userUtils.getUserEmail());
        boolean exists = importDetailList.stream()
                .anyMatch(item -> item.getImportDetailId() == importEditRequest.getImportDetailId());
        if (!exists) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Ban khong co quyen chinh sua cai nay");
        }

        if (importEditRequest.getPrice() >= 0) {
            importDetail.setPrice(importEditRequest.getPrice());
        }
        if (importEditRequest.getQuantity() >= 0) {
            importDetail.setQuantity(importEditRequest.getQuantity());
        }
        importDetail.setPriceTotal(importDetail.getQuantity() * importDetail.getPrice());
        importDetail.setUpdatedAt(UserUtils.getCurrentDay());
        String storeName = importDetail.getProduct().getStore().getStoreName();
        return convertToImportResponse(importDetailRepo.save(importDetail),storeName);

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
