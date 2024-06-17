package com.example.secumix.repository;



import com.example.secumix.entities.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepo extends JpaRepository<OrderDetail, Integer> {
    @Query("select o from orderdetail o where o.user.email=:email")
    Page<OrderDetail> getAllByUser(String email, Pageable pageable);

    @Query("select o from orderdetail o where o.user.email=:email and o.orderStatus.orderStatusId=1")
    Page<OrderDetail> getAllByUserSuccess(String email, Pageable paging);



    @Query("select o from orderdetail o where o.user.id=:userId")
    List<OrderDetail> getAllByUser(int userId);


    @Query("select o from orderdetail o where o.orderDetailId=:orderdetailid and o.user.email=:email")
    Optional<OrderDetail> findByIDandUser(int orderdetailid, String email);
    @Query("select o from orderdetail o where o.store.storeName=:storeName")
    List<OrderDetail> getAllByStore(String storeName);
    @Query("select o from orderdetail  o where o.shipperid=:shipperid and o.orderStatus.orderStatusId=2")
    List<OrderDetail> getOrderDetailByShipperId(int shipperid);

    @Query("select sum(o.priceTotal) from orderdetail o where o.orderStatus.orderStatusId=3 and o.store.storeId=:storeId")
    long RevenueByStore(int storeId);

    @Query("select sum(o.priceTotal) from orderdetail o where o.orderStatus.orderStatusId=3 and o.user.id=:userId and o.store.storeId=:storeId")
    Long RevenueByStoreAndUser(int userId, int storeId);


    @Query("select count(o) from orderdetail o where  o.user.id=:userId and o.store.storeId=:storeId")
    int totalOrderByStoreAndUser(int userId, int storeId);

    @Query("select o from orderdetail o where  o.user.id=:userId and o.store.storeId=:storeId")
    List<OrderDetail> listOrderByStoreAndUser(int userId, int storeId);


    @Query("select o from orderdetail o where o.store.storeName=:storeName AND o.user.id=:customerid")
    List<OrderDetail> findAllOrderByCustomerAndStorePaginable(String storeName, int customerid);

//    @Query("select o.productName from orderdetail o where o.storeName=:storeName AND o.user.id=:customerid")
//    List<String> findAllOrderByCustomerAndStorePaginable( String storeName, int customerid);


    @Query("select o from orderdetail o where o.store.storeName=:storeName AND o.user.id=:customerid " +
            "AND LOWER(o.product.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(o.orderStatus.orderStatusName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<OrderDetail> findOrderByTitleContainingIgnoreCase(String keyword, String storeName, int customerid);


    @Query("select o from orderdetail o where o.store.storeId=:storeId")
    List<OrderDetail> getAllByStoreWithPagination(int storeId);


    @Query("select o from orderdetail o where o.store.storeId=:storeId " +
            "AND LOWER(o.product.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(o.orderStatus.orderStatusName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<OrderDetail> findByTitleContainingIgnoreCase(int storeId, String keyword);

    @Query("select o from orderdetail o where o.orderStatus.orderStatusId=1 ")
    List<OrderDetail> findOrderNotShipped();
    @Query("select o from orderdetail o where o.shipperid=:shipperId and o.orderStatus=2 ")
    List<OrderDetail> findOrderReadyToShip(int shipperId);

    @Query("select o from orderdetail o where o.shipperid=:shipperId and o.orderStatus=3 ")
    List<OrderDetail> findOrderShipped(int shipperId);
    @Query("select o from orderdetail o where o.user.email=:email and o.orderStatus.orderStatusId=2")
    Page<OrderDetail> getAllByUserDelvery(String email, Pageable paging);
    @Query("select o from orderdetail o where o.user.email=:email and o.orderStatus.orderStatusId=3")
    Page<OrderDetail> getAllByUserShipped(String email, Pageable paging);
    @Query("select o from orderdetail o where o.user.email=:email and o.orderStatus.orderStatusId=4")

    Page<OrderDetail> getAllByUserCancel(String email, Pageable paging);
}
