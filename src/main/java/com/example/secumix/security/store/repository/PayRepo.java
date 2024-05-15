package com.example.secumix.security.store.repository;


import com.example.secumix.security.store.model.entities.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayRepo extends JpaRepository<Pay, Integer>
{
    @Query("select o from pay o where o.orderDetail.storeName=:storename")
    List<Pay> GetAllPayByStore(String storename);
    @Query("select o from pay o where o.orderDetail.cart.user.email=:email")
    List<Pay> GetAllPAyByUser(String email);

}
