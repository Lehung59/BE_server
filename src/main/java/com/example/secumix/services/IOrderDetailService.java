package com.example.secumix.services;



import com.example.secumix.payload.dtos.OrderDetailDto;
import com.example.secumix.entities.CartItem;
import com.example.secumix.entities.OrderDetail;
import com.example.secumix.payload.request.OrderDetailRequest;
import com.example.secumix.payload.response.OrderDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IOrderDetailService {
    Page<OrderDetail> GetAllByUser(int page, int size, String orderStatus);
    Optional<OrderDetail> findByIDandUser(int orderdetailid);

    void Insert(OrderDetailRequest orderDatailRequest);

    void Save(OrderDetail orderDatail);

    void ChangeStatus1(int orderdetailid);

    void ChangeStatus2(int orderdetailid);

    void ChangeStatus3(int orderdetailid);

    void InsertIDR(OrderDetailRequest orderDetailRequest, CartItem cartItem);

    Optional<OrderDetailResponse> GetInfoOrder(int orderdetailid);

    List<OrderDetailResponse> getOrderDetailByShipperId(int id,int page, int size, String keyword);

    Page<OrderDetail> findAllOrderByCustomerAndStorePaginable( int storeid, int customerid, int page, int size);

    Page<OrderDetail> findOrderByTitleContainingIgnoreCase(String keyword,  int storeid, int customerid, int page, int size);

    Page<OrderDetail> findAllOrderPaginable( int storeid, int page, int size);

    Page<OrderDetail> findByTitleContainingIgnoreCase(String keyword,  int storeid, int page, int size);

    OrderDetailDto findDtoById(int orderDetailId);

    Page<OrderDetail> findOrderNotShipped(int page, int size, String keyword);

    Page<OrderDetail> findOrderReadyToShip(int shipperId,int page, int size, String keyword);

    Page<OrderDetail> findOrderShipped(int shipperId,int page, int size, String keyword);
}
