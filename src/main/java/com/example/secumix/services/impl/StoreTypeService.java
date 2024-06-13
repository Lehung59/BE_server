package com.example.secumix.services.impl;

import com.example.secumix.entities.StoreType;
import com.example.secumix.repository.StoreTypeRepo;
import com.example.secumix.services.IStoreTypeService;
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
