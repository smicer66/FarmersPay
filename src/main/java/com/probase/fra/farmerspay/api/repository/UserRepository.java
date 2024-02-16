package com.probase.fra.farmerspay.api.repository;

import com.probase.fra.farmerspay.api.enums.UserRole;
import com.probase.fra.farmerspay.api.models.User;
import com.probase.fra.farmerspay.api.models.UserRolePermission;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(String username);

    @Query("SELECT u FROM UserRolePermission u WHERE u.userRole = :roleName ORDER BY id ASC")
    List<UserRolePermission> findRolePermissionByRole(UserRole roleName, Pageable pageable);
}
