package com.example.secumix.security.store.services;

import com.example.secumix.security.store.model.entities.CartItem;
import com.example.secumix.security.store.model.request.CartItemRequest;
import com.example.secumix.security.store.model.response.CartItemResponse;

import java.util.List;
import java.util.Optional;

public interface ICartItemService {
    List<CartItemResponse> findByProduct(int productid);
    List<CartItemResponse> findByUser();
    Optional<CartItemResponse> finfByProductandUser(int productid);
    void Insert(CartItemRequest cartItemRequest);

    void Save(CartItem cartItem);
    Optional<CartItem> findByIdandUser(int cartitemid);
    boolean Delete(int cartitemid);
}
