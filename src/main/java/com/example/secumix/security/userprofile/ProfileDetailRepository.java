package com.example.secumix.security.userprofile;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileDetailRepository extends JpaRepository<ProfileDetail,Integer> {
    @Query("select o from profile o where o.email=:email")
    Optional<ProfileDetail> findProfileDetailBy(String email);
    @Query("select o from profile  o where o.user.id=:userID")
    ProfileDetail findByUserID(int userID);
}
