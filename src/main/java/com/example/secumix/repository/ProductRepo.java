package com.example.secumix.repository;

import com.example.secumix.entities.Product;
import com.example.secumix.entities.ProfileDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
    @Query("select o from product o where o.store.storeId=:storeid and o.productName=:name")
    Optional<Product> findByName(int storeid, String name);



    @Query("select o from product o where o.productName=:name")
    List<Product> findByProductName( String name);
    @Query("select o from product o where o.store.emailmanager=:email")
    List<Product> getAllByStore(String email);
    @Query("select o from product o where o.productName like %:key%")
    List<Product> SearchByKey(String key);
    @Query("select o from product o where o.productType.productTypeId=:producttypeid")
    List<Product> findByProductType(int producttypeid);
    @Query("SELECT DISTINCT p FROM product p WHERE p.store.storeId = :storeId AND LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> findByTitleContainingIgnoreCase(int storeId, String keyword, Pageable pageable);

    @Query("SELECT p FROM product p WHERE p.store.storeId = :storeId")
    Page<Product> getAllByStoreWithPagination(int storeId, Pageable pageable);
    @Query("SELECT o FROM profile o WHERE o.user.id=:customerid")
    ProfileDetail findByUserId(int customerid);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from product p where p.productId = :productId")
    Product findProductForUpdate(@Param("productId") int productId);


    @Query(value = "SELECT p FROM product p WHERE p.store.storeId=:storeid AND p.status=true")
    Page<Product> findSellingProduct(int storeid, Pageable paging);


    @Query(value = "SELECT p FROM product p WHERE p.store.storeId=:storeid AND p.status=true " +
            "AND (p.productName LIKE %:keyword% )" +
            "OR (p.description LIKE %:keyword% )"
    )
    Page<Product> findSellingProductKeyword(int storeid, Pageable paging, String keyword);
}
