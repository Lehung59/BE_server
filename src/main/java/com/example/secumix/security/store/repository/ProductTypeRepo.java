package com.example.secumix.security.store.repository;


import com.example.secumix.security.store.model.entities.ProductType;
import com.example.secumix.security.store.model.response.ProductTypeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductTypeRepo extends JpaRepository<ProductType, Integer> {
    @Query("select o from producttype o where o.productTypeName=:name and o.store.storeId=:storeId")
    Optional<ProductType> findProductTypeByName(String name, int storeId);
    @Query("select o from producttype o where o.store.storeId=:storeId")
    List<ProductType> findByStoreId(int storeId);
}
