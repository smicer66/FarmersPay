package com.probase.fra.farmerspay.api.service;

import com.probase.fra.farmerspay.api.models.District;
import com.probase.fra.farmerspay.api.models.Province;
import com.probase.fra.farmerspay.api.repository.DistrictRepository;
import com.probase.fra.farmerspay.api.repository.ProvinceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilityService {

    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private DistrictRepository districtRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());



    public List<District> fetchDistricts() {
        List<District> districtList = districtRepository.fetchDistricts();
        return districtList;
    }

    public List<District> fetchDistrictByProvinceId(Long provinceId) {
        List<District> districtList = districtRepository.fetchDistrictByProvinceId(provinceId);
        return districtList;
    }



    public List<Province> fetchProvinces() {
        List<Province> provinceList = provinceRepository.fetchProvinces();
        return provinceList;
    }
}
