package com.example.secumix.repository;


import com.example.secumix.entities.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayRepo extends JpaRepository<Pay, Integer>
{
    @Query("select o from pay o where o.orderDetail.store.storeName=:storename")
    List<Pay> GetAllPayByStore(String storename);
    @Query("select o from pay o where o.orderDetail.user.email=:email")
    List<Pay> GetAllPAyByUser(String email);

}
