package com.example.secumix.services;

import com.example.secumix.exception.CustomException;
import com.example.secumix.entities.ImportDetail;
import com.example.secumix.payload.request.ImportEditRequest;
import com.example.secumix.payload.response.ImportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IImportDetailService {
    Optional<ImportDetail> findByStore(int storeid);

    List<ImportDetail> findByStoreandProduct(int storeid, String productname);

    Page<ImportDetail> findAllImportPaginable( int storeid,int page, int size);

    Page<ImportDetail> findImportByTitleContainingIgnoreCase(String keyword,int page, int size, int storeid);

    ImportResponse updateImport(ImportEditRequest importEditRequest) throws CustomException;
}
