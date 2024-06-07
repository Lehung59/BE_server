package com.example.secumix.security.Utils.DtoMapper;

import com.example.secumix.security.Exception.CustomException;
import com.example.secumix.security.store.model.dtos.OrderDetailDto;
import com.example.secumix.security.store.model.entities.*;
import com.example.secumix.security.store.repository.*;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserRepository;
import com.example.secumix.security.userprofile.ProfileDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

    @Autowired
    private ProfileDetailRepo profileDetailRepo;
    @Autowired
    private StoreRepo storeRepo;

    public OrderDetailDto toDto(OrderDetail orderDetail) {
        if (orderDetail == null) {
            return null;
        }
        ProfileDetail profileDetail = profileDetailRepo.findByUserId(orderDetail.getUser().getId());
        Product product = orderDetail.getProduct();
        Store store = orderDetail.getStore();
        OrderDetailDto orderDetailDto = new OrderDetailDto();
        orderDetailDto.setOrderDetailId(orderDetail.getOrderDetailId());
        orderDetailDto.setCreatedAt(orderDetail.getCreatedAt());
        orderDetailDto.setUpdatedAt(orderDetail.getUpdatedAt());
        orderDetailDto.setQuantity(orderDetail.getQuantity());
        orderDetailDto.setProductName(product.getProductName());
        orderDetailDto.setStoreName(store.getStoreName());
        orderDetailDto.setCustomerPhone(profileDetail.getPhoneNumber());
        orderDetailDto.setCustomerName(profileDetail.getFirstname() + " " + profileDetail.getLastname());
        orderDetailDto.setStoreId(orderDetail.getStore().getStoreId());
        orderDetailDto.setPriceTotal(orderDetail.getPriceTotal());
        orderDetailDto.setProductId(orderDetail.getProduct() != null ? orderDetail.getProduct().getProductId() : 0);
        orderDetailDto.setCartId(
                Optional.ofNullable(orderDetail.getCart())
                        .flatMap(cart -> Optional.of(cart.getCartId()))
                        .orElse(0)
        );
        orderDetailDto.setUserId(orderDetail.getUser() != null ? orderDetail.getUser().getId() : 0);
        orderDetailDto.setShipperId(orderDetail.getShipperid());
        orderDetailDto.setOrderStatusId(orderDetail.getOrderStatus() != null ? orderDetail.getOrderStatus().getOrderStatusId() : 0);

        return orderDetailDto;
    }

    public OrderDetail toEntity(OrderDetailDto orderDetailDto) {
        if (orderDetailDto == null) {
            return null;
        }
        Store store = storeRepo.findStoreById(orderDetailDto.getStoreId()).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"Khong tim thay cua hang"));
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderDetailId(orderDetailDto.getOrderDetailId());
        orderDetail.setCreatedAt(orderDetailDto.getCreatedAt());
        orderDetail.setUpdatedAt(orderDetailDto.getUpdatedAt());
        orderDetail.setQuantity(orderDetailDto.getQuantity());
        orderDetail.setStore(store);
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