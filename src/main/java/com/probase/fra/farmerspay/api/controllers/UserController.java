package com.probase.fra.farmerspay.api.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.probase.fra.farmerspay.api.enums.FarmersPayResponseCode;
import com.probase.fra.farmerspay.api.enums.UserRole;
import com.probase.fra.farmerspay.api.enums.UserStatus;
import com.probase.fra.farmerspay.api.exceptions.FarmersPayAuthException;
import com.probase.fra.farmerspay.api.models.ErrorMessage;
import com.probase.fra.farmerspay.api.models.User;
import com.probase.fra.farmerspay.api.models.UserType;
import com.probase.fra.farmerspay.api.models.requests.AddUserTypeRequest;
import com.probase.fra.farmerspay.api.models.requests.DataTablesRequest;
import com.probase.fra.farmerspay.api.models.requests.RegisterRequest;
import com.probase.fra.farmerspay.api.models.responses.FarmersPayResponse;
import com.probase.fra.farmerspay.api.providers.TokenProvider;
import com.probase.fra.farmerspay.api.service.UserService;
import com.probase.fra.farmerspay.api.service.UserTypeService;
import com.probase.fra.farmerspay.util.UtilityHelper;
import io.swagger.annotations.*;
import jakarta.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
@Api(produces = "application/json", description = "User management")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private UserTypeService userTypeService;
    @Autowired
    private TokenProvider jwtTokenUtil;


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






    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer <Token>")
    @ApiOperation(value = "List User Types", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/list-user-types/{pageSize}/{pageNumber}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getFarmList(
            @PathVariable Integer pageSize,
            @PathVariable Integer pageNumber,
//                                    @RequestParam(required = false) String searchString,
            DataTablesRequest listFarmsRequest,
            //BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        logger.info("{}....{}", pageNumber, pageSize);

        User authenticatedUser = jwtTokenUtil.getUserFromToken(request);




        Map userTypesList = userService.getAllUserTypes(listFarmsRequest, pageSize, pageNumber);



        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
        farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
        farmersPayResponse.setResponseData(userTypesList);
        farmersPayResponse.setMessage("User types list fetched");
        return ResponseEntity.ok().body(farmersPayResponse);
    }




    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer <Token>")
    @ApiOperation(value = "Add New User Types", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/add-user-type", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addUserType(@RequestBody @Valid AddUserTypeRequest addUserTypeRequest, BindingResult bindingResult,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws FarmersPayAuthException, JsonProcessingException {
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

        User authenticatedUser = jwtTokenUtil.getUserFromToken(request);


        List<UserType> userTypeList = addUserTypeRequest.getUserTypes().stream().map(t -> {
            UserType ut = userTypeService.getUserTypeByName(t);
            if(ut==null) {
                UserType userType = new UserType();
                userType.setCreatedByUserId(authenticatedUser.getId());
                userType.setUserRole(UserRole.ADMINISTRATOR);
                userType.setCreatedByFullName(authenticatedUser.getFirstName().concat(" ").concat(authenticatedUser.getLastName()));
                userType.setCreatedAt(LocalDateTime.now());
                userType.setUpdatedAt(LocalDateTime.now());
                userType.setUserType(t);
                userType = userTypeService.save(userType);

                return userType;
            }
            return null;
        }).filter(t -> t!=null).collect(Collectors.toList());

        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
        if(!userTypeList.isEmpty())
        {
            farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
            farmersPayResponse.setResponseData(userTypeList);
            farmersPayResponse.setMessage("User types provided were created successfully");
            return ResponseEntity.badRequest().body(farmersPayResponse);
        }

        farmersPayResponse.setResponseCode(FarmersPayResponseCode.GENERAL_ERROR.label);
        farmersPayResponse.setResponseData(null);
        farmersPayResponse.setMessage("None of the user types provided has been created. Confirm the user types provided do not already exist");
        return ResponseEntity.badRequest().body(farmersPayResponse);


    }

}
