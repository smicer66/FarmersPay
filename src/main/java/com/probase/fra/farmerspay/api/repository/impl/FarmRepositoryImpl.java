package com.probase.fra.farmerspay.api.repository.impl;

import com.probase.fra.farmerspay.api.models.Farm;
import com.probase.fra.farmerspay.api.models.FarmDTO;
import com.probase.fra.farmerspay.api.repository.FarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class FarmRepositoryImpl  {

    @Autowired
    private EntityManagerFactory emf;
//    @Query("SELECT f, p.provinceName, d.districtName FROM FarmDTO f, Province p, District d WHERE f.farmProvinceId = p.id AND f.farmDistrictId = d.id AND " +
//            "f.ownedByUserId = :userId AND f.deletedAt IS NULL ORDER by f.id DESC")

    //    @Query("SELECT new com.probase.fra.farmerspay.api.models.FarmDTO(f, p.provinceName, d.districtName) FROM Farm f, Province p, District d WHERE f.farmProvinceId = p.id AND f.farmDistrictId = d.id AND " +
//            "f.ownedByUserId = :userId AND f.deletedAt IS NULL ORDER by f.id DESC")
    public Integer findFarmsCountByUserId(@Param("userId") Long userId, Pageable pageable){
        EntityManager entityManager = emf.createEntityManager();
        Query  query = entityManager
                .createQuery("SELECT COUNT(f) as c FROM Farm f WHERE f.ownedByUserId = :userId AND f.deletedAt IS NULL ORDER by f.id DESC");
        query.setParameter("userId", userId);
        //query.set
        List<Integer> countSizeList = query.getResultList();
        Integer count = countSizeList.get(0);

        return count;
    }
}
