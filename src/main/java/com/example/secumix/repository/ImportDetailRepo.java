package com.example.secumix.repository;

import com.example.secumix.entities.ImportDetail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ImportDetailRepo extends JpaRepository<ImportDetail, Integer> {
    @Query("select o from importdt o where  o.product.store.storeId=:storeid")
    List<ImportDetail> findByStore(int storeid);

    @Query("select o from importdt o where  o.product.store.emailmanager=:email")
    List<ImportDetail> findByStore(String email);



    @Query("select o from importdt o where  o.product.store.storeId=:storeid and o.product.productName=:productname")
    List<ImportDetail> findByStoreandProduct(int storeid, String productname);
    @Query("SELECT i FROM importdt i WHERE i.product.store.storeId = :storeId")
    Page<ImportDetail> getAllImportByStoreWithPagination(int storeId, Pageable pageable);

    @Query("SELECT i FROM importdt i WHERE i.product.store.storeId = :storeId AND " +
            "LOWER(i.product.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) ")
    Page<ImportDetail> findImportByTitleContainingIgnoreCase(int storeId,Pageable pageable, String keyword);
}
