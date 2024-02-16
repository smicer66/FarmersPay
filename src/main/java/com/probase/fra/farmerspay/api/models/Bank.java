package com.probase.fra.farmerspay.api.models;

import com.probase.fra.farmerspay.api.enums.FarmStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name = "banks")
public class Bank implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable= false)
    private String bankName;
    @Column(nullable= false)
    private String bankCode;
    @Column(nullable= true)
    private Boolean mobileMoney;
    @Column(nullable= false)
    private LocalDateTime createdAt;
    @Column(nullable= true)
    private LocalDateTime deletedAt;
    @Column(nullable= false)
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
