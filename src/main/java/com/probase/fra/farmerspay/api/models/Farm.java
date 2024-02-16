package com.probase.fra.farmerspay.api.models;

import com.probase.fra.farmerspay.api.converters.FarmerStatusConverter;
import com.probase.fra.farmerspay.api.enums.FarmStatus;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name = "farms")
public class Farm implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable= false)
    private Long ownedByUserId;
    @Column(nullable= false)
    private String farmName;
    @Column(nullable= true)
    private String farmAddress;
    @Column(nullable= false)
    private Long farmDistrictId;
    @Column(nullable= false)
    private Long farmProvinceId;
    @Column(nullable= false)
    private LocalDateTime createdAt;
    @Column(nullable= true)
    private LocalDateTime deletedAt;
    @Column(nullable= false)
    private LocalDateTime updatedAt;

    @Column(nullable= false)
    @Enumerated(EnumType.STRING)
//    @Convert(converter = FarmerStatusConverter.class)
    private FarmStatus farmStatus;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
