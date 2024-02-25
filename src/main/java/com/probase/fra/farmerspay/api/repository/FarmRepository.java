package com.probase.fra.farmerspay.api.repository;

import com.probase.fra.farmerspay.api.models.Farm;
import com.probase.fra.farmerspay.api.models.FarmDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FarmRepository extends JpaRepository<Farm, Long>
{

//    @Query("SELECT f, p.provinceName, d.districtName FROM FarmDTO f, Province p, District d WHERE f.farmProvinceId = p.id AND f.farmDistrictId = d.id AND " +
//            "f.ownedByUserId = :userId AND f.deletedAt IS NULL ORDER by f.id DESC")

    @Query("SELECT new com.probase.fra.farmerspay.api.models.FarmDTO(f, p.provinceName, d.districtName) FROM Farm f, Province p, District d WHERE f.farmProvinceId = p.id AND f.farmDistrictId = d.id AND " +
            "f.ownedByUserId = :userId AND f.deletedAt IS NULL ORDER by f.id DESC")
    List<FarmDTO> findFarmsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT new com.probase.fra.farmerspay.api.models.FarmDTO(f, p.provinceName, d.districtName) FROM Farm f, Province p, District d WHERE f.farmProvinceId = p.id AND f.farmDistrictId = d.id AND " +
            "f.ownedByUserId = :userId AND f.deletedAt IS NULL AND " +
            "(f.farmName LIKE :farmName OR f.farmAddress LIKE :farmAddress OR d.districtName LIKE :districtName OR p.provinceName LIKE :provinceName)" +
            " ORDER by f.id DESC")
    List<FarmDTO> filterFarmsByUserId(@Param("userId") Long userId, String farmName, String farmAddress, String districtName, String provinceName, Pageable pageable);


    @Query("SELECT COUNT(*) as c FROM Farm f WHERE f.ownedByUserId = :userId AND f.deletedAt IS NULL")
    List<Integer> findFarmsCountByUserId(@Param("userId") Long userId);


    @Query("SELECT COUNT(f.id) as count FROM Farm f, Province p, District d WHERE f.farmProvinceId = p.id AND f.farmDistrictId = d.id AND " +
            "f.ownedByUserId = :userId AND f.deletedAt IS NULL AND " +
            "(f.farmName LIKE :farmName OR f.farmAddress LIKE :farmAddress OR d.districtName LIKE :districtName OR p.provinceName LIKE :provinceName)" +
            "")
    List<Integer> findFarmsCountByUserId(@Param("userId") Long userId, @Param("farmName") String farmName, @Param("farmAddress") String farmAddress, @Param("districtName") String districtName, @Param("provinceName") String provinceName);

    @Query("SELECT f, p.provinceName, d.districtName FROM Farm f, Province p, District d WHERE f.farmProvinceId = p.id AND f.farmDistrictId = d.id AND f.deletedAt IS NULL ORDER by f.id DESC")
    List<FarmDTO> findAllFarms(Pageable pageable);

    @Query("SELECT COUNT(*) as c FROM Farm f WHERE f.deletedAt IS NULL ORDER by f.id DESC")
    List<Integer> findAllFarmsCount();
}
