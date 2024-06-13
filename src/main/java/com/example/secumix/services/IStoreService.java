package com.example.secumix.services;

import com.example.secumix.entities.Store;
import com.example.secumix.payload.request.StoreInfoEditRequest;
import com.example.secumix.payload.request.StoreViewResponse;
import com.example.secumix.payload.response.ProductResponse;
import com.example.secumix.payload.response.StoreFavorRespone;
import com.example.secumix.payload.response.StoreInfoView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
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

    List<StoreInfoView> findFavorStore(int userId, String keyword, int page, int size);

    void removeStoreFromFavorList(int storeid, int userId);

    List<ProductResponse> findSellingProduct(int storeid, String keyword, int page, int size);
}
