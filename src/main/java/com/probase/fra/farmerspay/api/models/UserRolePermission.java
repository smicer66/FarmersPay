package com.probase.fra.farmerspay.api.models;

import com.probase.fra.farmerspay.api.enums.Permission;
import com.probase.fra.farmerspay.api.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_role_permissions")
public class UserRolePermission implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable= false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(nullable= false)
    @Enumerated(EnumType.STRING)
    private Permission permission;

    private String permissionDescription;

    @Column(nullable = false)
    LocalDateTime createdAt;
    @Column(nullable = true)
    LocalDateTime deletedAt;
    @Column(nullable = false)
    LocalDateTime updatedAt;
}
