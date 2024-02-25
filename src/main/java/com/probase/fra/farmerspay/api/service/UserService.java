package com.probase.fra.farmerspay.api.service;

import com.probase.fra.farmerspay.api.enums.UserRole;
import com.probase.fra.farmerspay.api.models.FarmDTO;
import com.probase.fra.farmerspay.api.models.User;
import com.probase.fra.farmerspay.api.models.UserRolePermission;
import com.probase.fra.farmerspay.api.models.UserType;
import com.probase.fra.farmerspay.api.models.requests.DataTablesRequest;
import com.probase.fra.farmerspay.api.repository.UserRepository;
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

    public User getUserById(Long userId) {
        User user = userRepository.getById(userId);
        return  user;
    }

    public Map getAllUserTypes(DataTablesRequest listFarmsRequest, Integer pageSize, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<UserType> userTypeList = new ArrayList<UserType>();


//        logger.info("xxx {}", listFarmsRequest.getDraw());
        if(listFarmsRequest!=null && listFarmsRequest.getSearch()!=null && listFarmsRequest.getSearch().containsKey("value")) {
            logger.info("xxx {}", listFarmsRequest.getDraw());
            logger.info("xxx {}", listFarmsRequest.getSearch());
            String searchStringLike = "%".concat(listFarmsRequest.getSearch().get("value")).concat("%");
            userTypeList = userRepository.filterUserTypes(searchStringLike, searchStringLike, pageable);
        }
        else {
            userTypeList = userRepository.findUserTypes(pageable);
        }


        List<Integer> count = new ArrayList<Integer>();
        if(listFarmsRequest!=null && listFarmsRequest.getSearch()!=null && listFarmsRequest.getSearch().containsKey("value")) {

            String searchStringLike = "%".concat(listFarmsRequest.getSearch().get("value")).concat("%");
            count = userRepository.filterUserTypesCount(searchStringLike, searchStringLike);
        }
        else {
            count = userRepository.findUserTypesCount();
        }


        logger.info("{}", count);

        Map map = new HashMap<>();
        map.put("userTypeList", userTypeList);
        map.put("count", count);

        return map;
    }
}
