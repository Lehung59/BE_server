package com.example.secumix.security.store.repository;



import com.example.secumix.security.store.model.entities.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepo extends JpaRepository<OrderDetail, Integer> {
    @Query("select o from orderdetail o where o.cart.user.email=:email")
    List<OrderDetail> getAllByUser(String email);

    @Query("select o from orderdetail o where o.cart.user.id=:userId")
    List<OrderDetail> getAllByUser(int userId);


    @Query("select o from orderdetail o where o.orderDetailId=:orderdetailid and o.cart.user.email=:email")
    Optional<OrderDetail> findByIDandUser(int orderdetailid, String email);
    @Query("select o from orderdetail o where o.storeName=:storeName")
    List<OrderDetail> getAllByStore(String storeName);
    @Query("select o from orderdetail  o where o.shipperid=:shipperid and o.orderStatus.orderStatusId=2")
    List<OrderDetail> getOrderDetailByShipperId(int shipperid);

    @Query("select sum(o.priceTotal) from orderdetail o where o.orderStatus.orderStatusId=3 and o.storeName=:storeId")
    long RevenueByStore(int storeId);

    @Query("select sum(o.priceTotal) from orderdetail o where o.orderStatus.orderStatusId=3 and o.user.id=:userId and o.storeId=:storeId")
    long RevenueByStoreAndUser(int userId, int storeId);


    @Query("select count(o) from orderdetail o where o.orderStatus.orderStatusId=3 and o.user.id=:userId and o.storeId=:storeId")
    int totalOrderByStoreAndUser(int userId, int storeId);

    @Query("select o from orderdetail o where o.orderStatus.orderStatusId=3 and o.user.id=:userId and o.storeId=:storeId")
    List<OrderDetail> listOrderByStoreAndUser(int userId, int storeId);


    @Query("select o from orderdetail o where o.storeName=:storeName AND o.user.id=:customerid")
    Page<OrderDetail> findAllOrderByCustomerAndStorePaginable(Pageable pageable, String storeName, int customerid);

    @Query("select o.productName from orderdetail o where o.storeName=:storeName AND o.user.id=:customerid")
    List<String> findAllOrderByCustomerAndStorePaginable( String storeName, int customerid);


    @Query("select o from orderdetail o where o.storeName=:storeName AND o.user.id=:customerid " +
            "AND LOWER(o.product.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(o.orderStatus.orderStatusName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<OrderDetail> findOrderByTitleContainingIgnoreCase(String keyword, Pageable pageable, String storeName, int customerid);
}
