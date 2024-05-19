package com.example.secumix.security.store.repository;


import com.example.secumix.security.store.model.entities.Store;
import com.example.secumix.security.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepo extends JpaRepository<Store, Integer> {
    @Query("select o from store o where o.phoneNumber=:phonenumber")
    Optional<Store> findByPhonenumber(String phonenumber);
    @Query("select o from store o where o.storeId=:storeId")
    Optional<Store> findStoreById(int storeId);

    @Query("select u from User u join u.stores s where s.storeId = :storeId")
    List<User> findCustomerByStoreId(int storeId);


    @Query("select o from store o where o.storeName=:storeName")
    Optional<Store> findStoreByName(String storeName);

    @Query("select o from store o where o.emailmanager=:email")
    Optional<Store> findByEmailManager(String email);

    @Query(" SELECT i FROM store i WHERE LOWER(i.storeName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(i.storeName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(i.emailmanager) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(i.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) "
    )
    Page<Store> findStoreByTitleContainingIgnoreCase(String keyword, Pageable pageable);



    @Query("SELECT i FROM store i ")
    Page<Store> getAllStoreWithPagination(Pageable pageable);
}
