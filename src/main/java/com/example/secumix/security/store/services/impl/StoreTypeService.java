package com.example.secumix.security.store.services.impl;

import com.example.secumix.security.store.model.entities.StoreType;
import com.example.secumix.security.store.repository.StoreTypeRepo;
import com.example.secumix.security.store.services.IStoreTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreTypeService implements IStoreTypeService {
    private final StoreTypeRepo storeTypeRepo;

    @Override
    public List<StoreType> getAll() {
        return storeTypeRepo.findAll();
    }
}
