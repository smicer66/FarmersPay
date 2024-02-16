package com.probase.fra.farmerspay.api.repository;

import com.probase.fra.farmerspay.api.models.District;
import com.probase.fra.farmerspay.api.models.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProvinceRepository extends JpaRepository<Province, Long> {



    @Query("SELECT f FROM Province f WHERE f.deletedAt IS NULL")
    List<Province> fetchProvinces();

}
