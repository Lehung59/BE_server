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
    List<OrderDetailResponse> GetAllByUser();
    Optional<OrderDetail> findByIDandUser(int orderdetailid);

    void Insert(OrderDetailRequest orderDatailRequest);

    void Save(OrderDetail orderDatail);

    void ChangeStatus1(int orderdetailid);

    void ChangeStatus2(int orderdetailid);

    void ChangeStatus3(int orderdetailid);

    void InsertIDR(OrderDetailRequest orderDetailRequest, CartItem cartItem);

    Optional<OrderDetailResponse> GetInfoOrder(int orderdetailid);

    List<OrderDetailResponse> getOrderDetailByShipperId(int id);

    Page<OrderDetailResponse> findAllOrderByCustomerAndStorePaginable(Pageable paging, int storeid, int customerid);

    Page<OrderDetailResponse> findOrderByTitleContainingIgnoreCase(String keyword, Pageable paging, int storeid, int customerid);

    Page<OrderDetailResponse> findAllOrderPaginable(Pageable paging, int storeid);

    Page<OrderDetailResponse> findByTitleContainingIgnoreCase(String keyword, Pageable paging, int storeid);

    OrderDetailDto findDtoById(int orderDetailId);

    List<OrderDetailResponse> findOrderNotShipped();

    List<OrderDetailResponse> findOrderReadyToShip(int shipperId);

    List<OrderDetailResponse> findOrderShipped(int shipperId);
}
