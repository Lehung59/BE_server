package com.example.secumix.services;

import com.example.secumix.entities.CartItem;
import com.example.secumix.payload.request.CartItemRequest;
import com.example.secumix.payload.response.CartItemResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ICartItemService {
    Page<CartItem> findByUser(int page, int size);
    void Insert(CartItemRequest cartItemRequest);

    void Save(CartItem cartItem);
    Optional<CartItem> findByIdandUser(int cartitemid);
    boolean Delete(int cartitemid);

    void updateCartItem(int cartItemid, int quantity);
}
