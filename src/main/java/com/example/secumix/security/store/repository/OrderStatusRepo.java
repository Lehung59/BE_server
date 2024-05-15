package com.example.secumix.security.store.repository;


import com.example.secumix.security.store.model.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderStatusRepo extends JpaRepository<OrderStatus, Integer> {
    @Query("select o from orderstatus o where  o.orderStatusName=:name")
    Optional<OrderStatus> findbyName(String name);
}
