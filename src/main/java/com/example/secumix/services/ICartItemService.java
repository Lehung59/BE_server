package com.example.secumix.services;

import com.example.secumix.entities.CartItem;
import com.example.secumix.payload.request.CartItemRequest;
import com.example.secumix.payload.response.CartItemResponse;

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

    void updateCartItem(int cartItemid, int quantity);
}
