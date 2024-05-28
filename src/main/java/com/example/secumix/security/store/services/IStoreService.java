package com.example.secumix.security.store.services;

import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.store.model.dtos.StoreDto;
import com.example.secumix.security.store.model.entities.Store;
import com.example.secumix.security.store.model.request.StoreInfoEditRequest;
import com.example.secumix.security.store.model.request.StoreViewResponse;
import com.example.secumix.security.store.model.response.StoreInfoView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface IStoreService {
    Optional<Store> findStoreById(int storeId);

    void checkStoreAuthen(int storeid);

    Optional<Store> findStoreByEmail(String email);

    void updateInfo(StoreInfoEditRequest storeInfoEditRequest);

    Page<StoreViewResponse> findAllStorePaginable(Pageable paging);

    Page<StoreViewResponse> findAllStoreByTitleContainingIgnoreCase(String keyword, Pageable paging);

    StoreInfoView getInfo(int storeid);

    void addStoreToFavor(int userId, int storeid);

    Page<StoreDto> viewFavor(int id, String keyword, int page, int size);
}
