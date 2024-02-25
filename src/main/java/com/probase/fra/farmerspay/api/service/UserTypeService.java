package com.probase.fra.farmerspay.api.service;

import com.probase.fra.farmerspay.api.enums.UserRole;
import com.probase.fra.farmerspay.api.models.User;
import com.probase.fra.farmerspay.api.models.UserRolePermission;
import com.probase.fra.farmerspay.api.models.UserType;
import com.probase.fra.farmerspay.api.models.requests.DataTablesRequest;
import com.probase.fra.farmerspay.api.repository.UserRepository;
import com.probase.fra.farmerspay.api.repository.UserTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserTypeService {

    @Autowired
    private UserTypeRepository userTypeRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public UserType save(UserType userType){
        return userTypeRepository.save(userType);
    }


    public UserType getUserTypeByName(String userTypeName) {
        return userTypeRepository.getUserTypeByName(userTypeName);
    }
}
