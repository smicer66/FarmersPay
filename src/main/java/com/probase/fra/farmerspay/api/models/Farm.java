package com.probase.fra.farmerspay.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.probase.fra.farmerspay.api.enums.FarmStatus;
import javax.persistence.*;

import com.probase.fra.farmerspay.api.serializer.JsonDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name = "farms")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class Farm implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable= false)
    private Long ownedByUserId;
    @Column(nullable= false)
    private String farmName;
    private String farmAddress;
    @Column(nullable= false)
    private Long farmDistrictId;
    @Column(nullable= false)
    private Long farmProvinceId;
    @Column(nullable= false)
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private LocalDateTime deletedAt;
    @Column(nullable= false)
    @JsonSerialize(using = JsonDateTimeSerializer.class)
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
