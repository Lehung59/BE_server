package com.example.secumix.security.store.services;

import com.example.secumix.security.store.model.entities.ImportDetail;
import com.example.secumix.security.store.model.response.ImportResponse;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IImportDetailService {
    Optional<ImportDetail> findByStore(int storeid);

    List<ImportDetail> findByStoreandProduct(int storeid, String productname);

    Page<ImportResponse> findAllImportPaginable(Pageable paging, int storeid);

    Page<ImportResponse> findImportByTitleContainingIgnoreCase(String keyword, Pageable paging, int storeid);
}
