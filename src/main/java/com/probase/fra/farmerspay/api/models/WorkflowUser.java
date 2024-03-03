package com.probase.fra.farmerspay.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.probase.fra.farmerspay.api.enums.FarmStatus;
import com.probase.fra.farmerspay.api.enums.UserRole;
import com.probase.fra.farmerspay.api.serializer.JsonDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name = "workflow_users")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class WorkflowUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable= false)
    private Long userId;
    @Column(nullable= false)
    private String userFullName;
    @Column(nullable= false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    private Integer level;
    @Column(nullable= false)
    private String permissionList;
    @Column(nullable= false)
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private LocalDateTime deletedAt;
    @Column(nullable= false)
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
