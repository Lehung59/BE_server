package com.example.secumix.security.store.repository;

import com.example.secumix.security.store.model.entities.CartItem;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Integer> {
    @Query("select o from cartitem o where o.product.productId=:productid")
    List<CartItem> findByProduct(int productid);
    @Query("select o from cartitem o where o.cart.user.email=:email")
    List<CartItem> findByUser(String email);
    @Query("select o from cartitem o where o.cart.user.email=:email and o.product.productId=:productid")
    Optional<CartItem> finfByProductandUser(int productid, String email);
    @Query("select o from cartitem o where o.cart.user.email=:email and o.cartItemId=:cartitemid")
    Optional<CartItem> findByitemidandUser(int cartitemid, String email);
}
