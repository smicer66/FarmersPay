package com.probase.fra.farmerspay.api.controllers;


import com.probase.fra.farmerspay.api.enums.FarmStatus;
import com.probase.fra.farmerspay.api.enums.FarmersPayResponseCode;
import com.probase.fra.farmerspay.api.enums.UserRole;
import com.probase.fra.farmerspay.api.enums.UserStatus;
import com.probase.fra.farmerspay.api.models.ErrorMessage;
import com.probase.fra.farmerspay.api.models.Farm;
import com.probase.fra.farmerspay.api.models.User;
import com.probase.fra.farmerspay.api.models.requests.RegisterRequest;
import com.probase.fra.farmerspay.api.models.responses.FarmersPayResponse;
import com.probase.fra.farmerspay.api.service.UserService;
import com.probase.fra.farmerspay.util.UtilityHelper;
import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
@Api(produces = "application/json", description = "User management")
public class UserController {


    @Autowired
    private UserService userService;


    @RequestMapping(value="/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerNewFarmer(@RequestBody @Valid RegisterRequest registerRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List errorMessageList =  bindingResult.getFieldErrors().stream().map(fe -> {
                return new ErrorMessage(fe.getField(), fe.getDefaultMessage());
            }).collect(Collectors.toList());

            FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
            farmersPayResponse.setResponseData(errorMessageList);
            farmersPayResponse.setResponseCode(FarmersPayResponseCode.VALIDATION_FAILED.label);
            farmersPayResponse.setMessage("Validation of registration form failed");
            return ResponseEntity.badRequest().body(farmersPayResponse);
        }

        String bcryptPassword = UtilityHelper.generateBCryptPassword(registerRequest.getPassword());
        String otp = RandomStringUtils.randomNumeric(6);

        User user = new User();
//        user.setCreatedAt(LocalDateTime.now());
//        user.setUpdatedAt(LocalDateTime.now());

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setOtherNames(registerRequest.getOtherNames());
        user.setUserRole(UserRole.FARMER);
        user.setGender(registerRequest.getGender());
        user.setUserStatus(UserStatus.NOT_ACTIVATED);
        user.setPassword(bcryptPassword);
        user.setDateOfBirth(registerRequest.getDateOfBirth());
        user.setMobileNumber(registerRequest.getMobileNumber());
        user.setUsername(registerRequest.getMobileNumber());
        user.setOtp(otp);

        user = userService.save(user);

        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
        farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
        farmersPayResponse.setResponseData(user);
        farmersPayResponse.setMessage("Your registration was successful. Please provide the OTP sent to your mobile number");
        return ResponseEntity.badRequest().body(farmersPayResponse);
    }


    @RequestMapping(value="/get-user-by-id/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserById(@PathVariable Long userId){


        User user = userService.getUserById(userId);

        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
        farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
        farmersPayResponse.setResponseData(user);
        farmersPayResponse.setMessage("User fetched");
        return ResponseEntity.ok().body(farmersPayResponse);
    }


}
