package com.example.secumix.repository;

import com.example.secumix.entities.AuthenticationType;
import com.example.secumix.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.authType = ?2 WHERE u.email = ?1")
    void updateAuthenticationType(String username, AuthenticationType authType);


}
