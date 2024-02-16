package com.probase.fra.farmerspay.api.service;

import com.probase.fra.farmerspay.api.enums.UserRole;
import com.probase.fra.farmerspay.api.models.Farm;
import com.probase.fra.farmerspay.api.models.User;
import com.probase.fra.farmerspay.api.models.UserRolePermission;
import com.probase.fra.farmerspay.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public User save(User user){
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user;
    }


    public List<UserRolePermission> getPermissionsByRole(UserRole roleName, Integer pageNumber, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<UserRolePermission> userRolePermissionList = userRepository.findRolePermissionByRole(roleName, pageable);
        return userRolePermissionList;
    }
}
