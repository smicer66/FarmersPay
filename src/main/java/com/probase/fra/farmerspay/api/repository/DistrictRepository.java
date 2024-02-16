package com.probase.fra.farmerspay.api.repository;

import com.probase.fra.farmerspay.api.models.District;
import com.probase.fra.farmerspay.api.models.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long> {

    @Query("SELECT f FROM District f WHERE f.deletedAt IS NULL")
    List<District> fetchDistricts();


    @Query("SELECT f FROM District f WHERE f.deletedAt IS NULL AND f.provinceId = :provinceId")
    List<District> fetchDistrictByProvinceId(Long provinceId);
}
