package com.example.secumix.security.Utils.DtoMapper;

import com.example.secumix.security.store.model.dtos.OrderDetailDto;
import com.example.secumix.security.store.model.entities.Cart;
import com.example.secumix.security.store.model.entities.OrderDetail;
import com.example.secumix.security.store.model.entities.OrderStatus;
import com.example.secumix.security.store.model.entities.Product;
import com.example.secumix.security.store.repository.CartRepo;
import com.example.secumix.security.store.repository.OrderStatusRepo;
import com.example.secumix.security.store.repository.ProductRepo;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailMapper {

    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private CartRepo cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderStatusRepo orderStatusRepository;

    public OrderDetailDto toDto(OrderDetail orderDetail) {
        if (orderDetail == null) {
            return null;
        }

        OrderDetailDto orderDetailDto = new OrderDetailDto();
        orderDetailDto.setOrderDetailId(orderDetail.getOrderDetailId());
        orderDetailDto.setCreatedAt(orderDetail.getCreatedAt());
        orderDetailDto.setUpdatedAt(orderDetail.getUpdatedAt());
        orderDetailDto.setQuantity(orderDetail.getQuantity());
        orderDetailDto.setProductName(orderDetail.getProductName());
        orderDetailDto.setStoreName(orderDetail.getStoreName());
        orderDetailDto.setStoreId(orderDetail.getStoreId());
        orderDetailDto.setPriceTotal(orderDetail.getPriceTotal());
        orderDetailDto.setProductId(orderDetail.getProduct() != null ? orderDetail.getProduct().getProductId() : 0);
        orderDetailDto.setCartId(orderDetail.getCart() != null ? orderDetail.getCart().getCartId() : 0);
        orderDetailDto.setUserId(orderDetail.getUser() != null ? orderDetail.getUser().getId() : 0);
        orderDetailDto.setShipperId(orderDetail.getShipperid());
        orderDetailDto.setOrderStatusId(orderDetail.getOrderStatus() != null ? orderDetail.getOrderStatus().getOrderStatusId() : 0);

        return orderDetailDto;
    }

    public OrderDetail toEntity(OrderDetailDto orderDetailDto) {
        if (orderDetailDto == null) {
            return null;
        }

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderDetailId(orderDetailDto.getOrderDetailId());
        orderDetail.setCreatedAt(orderDetailDto.getCreatedAt());
        orderDetail.setUpdatedAt(orderDetailDto.getUpdatedAt());
        orderDetail.setQuantity(orderDetailDto.getQuantity());
        orderDetail.setProductName(orderDetailDto.getProductName());
        orderDetail.setStoreName(orderDetailDto.getStoreName());
        orderDetail.setStoreId(orderDetailDto.getStoreId());
        orderDetail.setPriceTotal(orderDetailDto.getPriceTotal());

        if (orderDetailDto.getProductId() > 0) {
            Product product = productRepository.findById(orderDetailDto.getProductId()).orElse(null);
            orderDetail.setProduct(product);
        }

        if (orderDetailDto.getCartId() > 0) {
            Cart cart = cartRepository.findById(orderDetailDto.getCartId()).orElse(null);
            orderDetail.setCart(cart);
        }

        if (orderDetailDto.getUserId() > 0) {
            User user = userRepository.findById(orderDetailDto.getUserId()).orElse(null);
            orderDetail.setUser(user);
        }

        if (orderDetailDto.getOrderStatusId() > 0) {
            OrderStatus orderStatus = orderStatusRepository.findById(orderDetailDto.getOrderStatusId()).orElse(null);
            orderDetail.setOrderStatus(orderStatus);
        }

        orderDetail.setShipperid(orderDetailDto.getShipperId());

        return orderDetail;
    }
}