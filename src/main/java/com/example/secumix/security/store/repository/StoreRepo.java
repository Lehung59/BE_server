package com.example.secumix.security.store.repository;


import com.example.secumix.security.store.model.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepo extends JpaRepository<Store, Integer> {
    @Query("select o from store o where o.phoneNumber=:phonenumber")
    Optional<Store> findByPhonenumber(String phonenumber);
    @Query("select o from store o where o.storeId=:storeId")
    Optional<Store> findStoreById(int storeId);
    @Query("select o from store o where o.storeName=:storeName")
    Optional<Store> findStoreByName(String storeName);
    @Query("select o from store o where o.emailmanager=:email")
    Optional<Store> findByEmailManager(String email);
}
