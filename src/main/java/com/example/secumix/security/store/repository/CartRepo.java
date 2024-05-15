package com.example.secumix.security.store.repository;


import com.example.secumix.security.store.model.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepo extends JpaRepository<Cart, Integer> {
    @Query("select o from cart o where o.user.id=:userId")
    List<Cart> findByUserId(@Param("userId") Integer userId);
    @Query("select o from cart o where o.user.email=:email")
    Cart findByEmail(String email);
}
