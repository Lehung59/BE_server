package com.example.secumix.security.store.repository;


import com.example.secumix.security.store.model.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepo extends JpaRepository<ProductImage, Integer> {
}
