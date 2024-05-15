package com.example.secumix.security.store.repository;


import com.example.secumix.security.store.model.entities.StoreType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreTypeRepo extends JpaRepository<StoreType, Integer> {
    @Query("select o from storetype o where o.storeTypeName=:name")
    Optional<StoreType> findByName(String name);
}
