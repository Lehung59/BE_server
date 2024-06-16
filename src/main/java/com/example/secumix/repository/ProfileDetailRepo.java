package com.example.secumix.repository;

import com.example.secumix.entities.ProfileDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileDetailRepo extends JpaRepository<ProfileDetail, Integer> {
    @Query("SELECT p FROM profile p JOIN p.user u JOIN u.stores s WHERE s.storeId = :storeid")
    List<ProfileDetail> getAllCustomerByStoreWithPagination(int storeid);

//
//    @Query("SELECT p FROM profile p JOIN p.user u JOIN u.stores s WHERE s.storeId = :storeid")
//    List<ProfileDetail> getAllCustomerByStoreWithPagination(int storeid);

    @Query(" SELECT DISTINCT p FROM profile p JOIN p.user.stores s WHERE s.storeId = :storeid AND p.user.role = 'USER' AND " +
            " CONCAT(LOWER(p.firstname), ' ', LOWER(p.lastname)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "p.phoneNumber LIKE %:keyword%"
    )
    List<ProfileDetail> findCustomerByTitleContainingIgnoreCase(int storeid, String keyword);

    @Query(" select p from profile p where p.user.id=:id")
    ProfileDetail findByUserId(Integer id);

    @Query(" select p from profile p where p.user.id=:id")
    Optional<ProfileDetail> findByUserIdCheck(Integer id);
}
