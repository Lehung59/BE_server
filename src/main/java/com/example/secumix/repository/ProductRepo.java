package com.example.secumix.repository;

import com.example.secumix.entities.Product;
import com.example.secumix.entities.ProfileDetail;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
    @Query("select o from product o where o.store.storeId=:storeid and o.productName=:name and o.deleted=false")
    Optional<Product> findByName(int storeid, String name);



    @Query("select o from product o where o.productName=:name and o.deleted=false")
    List<Product> findByProductName( String name);
    @Query("select o from product o where o.store.emailmanager=:email and o.deleted=false")
    List<Product> getAllByStore(String email);
    @Query("select o from product o where o.productName like %:key% and o.deleted=false")
    List<Product> SearchByKey(String key);
    @Query("select o from product o where o.productType.productTypeId=:producttypeid and o.deleted=false")
    List<Product> findByProductType(int producttypeid);
    @Query("SELECT DISTINCT p FROM product p WHERE p.store.storeId = :storeId and p.deleted=false AND LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> findByTitleContainingIgnoreCase(int storeId, String keyword, Pageable pageable);

    @Query("SELECT p FROM product p WHERE p.store.storeId = :storeId and p.deleted=false")
    Page<Product> getAllByStoreWithPagination(int storeId, Pageable pageable);

    @Query("SELECT DISTINCT p FROM product p WHERE p.store.storeId = :storeId AND LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> findByTitleContainingIgnoreCaseAdmin(int storeId, String keyword, Pageable pageable);

    @Query("SELECT p FROM product p WHERE p.store.storeId = :storeId ")
    Page<Product> getAllByStoreWithPaginationAdmin(int storeId, Pageable pageable);


    @Query("SELECT o FROM profile o WHERE o.user.id=:customerid")
    ProfileDetail findByUserId(int customerid);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from product p where p.productId = :productId and p.deleted=false")
    Product findProductForUpdate(@Param("productId") int productId);


    @Query(value = "SELECT p FROM product p WHERE p.store.storeId=:storeid AND p.status=1 and p.deleted=false")
    Page<Product> findSellingProduct(int storeid, Pageable pageable);


    @Query(value = "SELECT p FROM product p WHERE p.store.storeId=:storeid AND p.status=1 and p.deleted=false " +
            "AND (p.productName LIKE %:keyword% )" +
            "OR (p.description LIKE %:keyword% )"
    )
    Page<Product> findSellingProductKeyword(int storeid, String keyword, Pageable pageable);

    @Query(value = "SELECT p from product p WHERE p.productId= :productId AND p.deleted=false")
    Optional<Product> findById(int productId);
    @Query(value = "SELECT p from product p WHERE p.productId= :productId")
    Optional<Product> findByIdAdmin(int productId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM product WHERE productid = :productId", nativeQuery = true)
    void deleteProductAdmin(@Param("productId") int productId);

}
