package com.example.secumix.security.store.repository;

import com.example.secumix.security.user.User;
import com.example.secumix.security.userprofile.ProfileDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileDetailRepo extends JpaRepository<ProfileDetail, Integer> {
    @Query("SELECT p FROM profile p JOIN p.user u JOIN u.stores s WHERE s.storeId = :storeid")
    Page<ProfileDetail> getAllCustomerByStoreWithPagination(int storeid, Pageable pageable);

    @Query("SELECT p FROM profile p JOIN p.user u JOIN u.stores s WHERE s.storeId = :storeid ")
    List<ProfileDetail> getAllCustomerByStoreWithPagination(int storeid);

//
//    @Query("SELECT p FROM profile p JOIN p.user u JOIN u.stores s WHERE s.storeId = :storeid")
//    List<ProfileDetail> getAllCustomerByStoreWithPagination(int storeid);

    @Query(" SELECT DISTINCT p FROM profile p JOIN p.user.stores s WHERE s.storeId = :storeid AND p.user.role = 'USER' AND " +
            " CONCAT(LOWER(p.firstname), ' ', LOWER(p.lastname)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "p.phoneNumber LIKE %:keyword%"
    )
    Page<ProfileDetail> findCustomerByTitleContainingIgnoreCase(int storeid, String keyword, Pageable pageable);
}
