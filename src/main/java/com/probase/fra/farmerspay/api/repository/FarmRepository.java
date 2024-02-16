package com.probase.fra.farmerspay.api.repository;

import com.probase.fra.farmerspay.api.models.Farm;
import com.probase.fra.farmerspay.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FarmRepository extends JpaRepository<Farm, Long> {

    @Query("SELECT f FROM Farm f WHERE f.ownedByUserId > :userId AND f.deletedAt IS NULL ORDER by f.id DESC LIMIT :pageNumber, :pageSize")
    List<Farm> findFarmsByUserId(@Param("userId") Long userId, @Param("pageSize") Integer pageSize, @Param("pageNumber") Integer pageNumber);


    @Query("SELECT f FROM Farm f WHERE f.deletedAt IS NULL ORDER by f.id DESC LIMIT :pageNumber, :pageSize")
    List<Farm> findAllFarms(@Param("pageSize") Integer pageSize, @Param("pageNumber") Integer pageNumber);
}
