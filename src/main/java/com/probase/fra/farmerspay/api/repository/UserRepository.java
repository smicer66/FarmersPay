package com.probase.fra.farmerspay.api.repository;

import com.probase.fra.farmerspay.api.enums.UserRole;
import com.probase.fra.farmerspay.api.models.User;
import com.probase.fra.farmerspay.api.models.UserDTO;
import com.probase.fra.farmerspay.api.models.UserRolePermission;
import com.probase.fra.farmerspay.api.models.UserType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(String username);

    @Query("SELECT u FROM UserRolePermission u WHERE u.userRole = :roleName ORDER BY id ASC")
    List<UserRolePermission> findRolePermissionByRole(UserRole roleName, Pageable pageable);

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



    @Query("SELECT new com.probase.fra.farmerspay.api.models.UserDTO(u, ut.userType) FROM User u LEFT JOIN UserType ut on u.userTypeId = ut.id WHERE u.deletedAt IS NULL")
    List<UserDTO> findUsers(Pageable pageable);



    @Query("SELECT new com.probase.fra.farmerspay.api.models.UserDTO(u, ut.userType) FROM User u LEFT JOIN UserType ut on u.userTypeId = ut.id WHERE u.deletedAt IS NULL AND " +
            "(u.firstName LIKE :searchStringLike OR u.lastName LIKE :searchStringLike1 OR " +
            "u.otherNames LIKE :searchStringLike2 OR u.userRole LIKE :searchStringLike3 OR " +
            "u.userStatus LIKE :searchStringLike4 OR u.mobileNumber LIKE :searchStringLike5) ORDER BY id ASC")
    List<UserDTO> filterUsers(String searchStringLike, String searchStringLike1, String searchStringLike2, String searchStringLike3, String searchStringLike4, String searchStringLike5, Pageable pageable);



    @Query("SELECT COUNT(u.id) as count FROM User u WHERE u.deletedAt IS NULL AND " +
            "(u.firstName LIKE :searchStringLike OR u.lastName LIKE :searchStringLike1 OR " +
            "u.otherNames LIKE :searchStringLike2 OR u.userRole LIKE :searchStringLike3 OR " +
            "u.userStatus LIKE :searchStringLike4 OR u.mobileNumber LIKE :searchStringLike5)")
    List<Integer> filterUsersCount(String searchStringLike, String searchStringLike1, String searchStringLike2, String searchStringLike3, String searchStringLike4, String searchStringLike5);



    @Query("SELECT COUNT(u.id) as count FROM User u WHERE u.deletedAt IS NULL")
    List<Integer> findUsersCount();


    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND u.username = :username")
    User getUserByUsername(String username);
}
