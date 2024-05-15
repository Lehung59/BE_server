package com.example.secumix.security.store.services;

import com.example.secumix.security.ResponseObject;
import com.example.secumix.security.store.model.entities.Store;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface IStoreService {
    Optional<Store> findStoreById(int storeId);

    void checkStoreAuthen(int storeid);

    Optional<Store> findStoreByEmail(String email);
}
