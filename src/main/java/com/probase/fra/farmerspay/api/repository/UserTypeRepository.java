package com.probase.fra.farmerspay.api.repository;

import com.probase.fra.farmerspay.api.enums.UserRole;
import com.probase.fra.farmerspay.api.models.UserRolePermission;
import com.probase.fra.farmerspay.api.models.UserType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserTypeRepository extends JpaRepository<UserType, Long> {


    @Query("SELECT u FROM UserType u WHERE u.deletedAt IS NULL ORDER BY id ASC")
    List<UserType> findUserTypes(Pageable pageable);

    @Query("SELECT COUNT(u.id) FROM UserType u WHERE u.deletedAt IS NULL")
    List<Integer> findUserTypesCount();

    @Query("SELECT u FROM UserType u WHERE u.deletedAt IS NULL AND " +
            "(u.userType LIKE :searchStringLike OR u.createdByFullName LIKE :searchStringLike1)" +
            " ORDER by u.id DESC")
    List<UserType> filterUserTypes(String searchStringLike, String searchStringLike1, Pageable pageable);


    @Query("SELECT COUNT(u.id) as count FROM UserType u WHERE u.deletedAt IS NULL AND " +
            "(u.userType LIKE :searchStringLike OR u.createdByFullName LIKE :searchStringLike1)")
    List<Integer> filterUserTypesCount(String searchStringLike, String searchStringLike1);


    @Query("SELECT u FROM UserType u WHERE u.deletedAt IS NULL AND u.userType = :userTypeName")
    UserType getUserTypeByName(String userTypeName);
}
