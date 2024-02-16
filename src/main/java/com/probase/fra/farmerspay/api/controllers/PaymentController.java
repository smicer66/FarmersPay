package com.probase.fra.farmerspay.api.controllers;


import com.probase.fra.farmerspay.api.enums.FarmStatus;
import com.probase.fra.farmerspay.api.enums.FarmersPayResponseCode;
import com.probase.fra.farmerspay.api.enums.UserRole;
import com.probase.fra.farmerspay.api.models.ErrorMessage;
import com.probase.fra.farmerspay.api.models.Farm;
import com.probase.fra.farmerspay.api.models.FarmBankAccount;
import com.probase.fra.farmerspay.api.models.User;
import com.probase.fra.farmerspay.api.models.requests.*;
import com.probase.fra.farmerspay.api.models.responses.FarmersPayResponse;
import com.probase.fra.farmerspay.api.providers.TokenProvider;
import com.probase.fra.farmerspay.api.service.FarmService;
import io.swagger.annotations.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payments")
@Api(produces = "application/json", description = "Payment management")
public class PaymentController {


    @Autowired
    private FarmService farmService;
    @Autowired
    private TokenProvider jwtTokenUtil;


    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer <Token>")
    @ApiOperation(value = "Bulk Pay Farms", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/bulk-pay-farms", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity bulkPayFarms(
            @RequestBody @Valid BulkPaymentRequest bulkPaymentRequest, BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User authenticatedUser = jwtTokenUtil.getUserFromToken(request);

        if (bindingResult.hasErrors()) {
            List errorMessageList =  bindingResult.getFieldErrors().stream().map(fe -> {
                return new ErrorMessage(fe.getField(), fe.getDefaultMessage());
            }).collect(Collectors.toList());

            FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
            farmersPayResponse.setResponseData(errorMessageList);
            farmersPayResponse.setResponseCode(FarmersPayResponseCode.VALIDATION_FAILED.label);
            farmersPayResponse.setMessage("Validation of farmer form failed");
            return ResponseEntity.badRequest().body(farmersPayResponse);
        }

        List<Farm> farmList = farmService.getAllFarms();
        farm.setFarmName(newFarmRequest.getFarmName());
        farm.setFarmStatus(FarmStatus.ACTIVE);
        farm.setFarmDistrictId(newFarmRequest.getFarmDistrictId());
        farm.setFarmProvinceId(newFarmRequest.getFarmProvinceId());
        farm.setFarmAddress(newFarmRequest.getFarmAddress());
        farm.setOwnedByUserId(authenticatedUser.getId());
        farm.setUpdatedAt(LocalDateTime.now());

        farm = farmService.save(farm);

        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
        farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
        farmersPayResponse.setResponseData(farm);
        farmersPayResponse.setMessage(message);
        return ResponseEntity.badRequest().body(farmersPayResponse);
    }


    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer <Token>")
    @ApiOperation(value = "Update farm status", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/update-farm-status", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateFarmStatus(
            @RequestBody @Valid UpdateFarmStatusRequest updateFarmStatusRequest, BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User authenticatedUser = jwtTokenUtil.getUserFromToken(request);

        if (bindingResult.hasErrors()) {
            List errorMessageList =  bindingResult.getFieldErrors().stream().map(fe -> {
                return new ErrorMessage(fe.getField(), fe.getDefaultMessage());
            }).collect(Collectors.toList());

            FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
            farmersPayResponse.setResponseData(errorMessageList);
            farmersPayResponse.setResponseCode(FarmersPayResponseCode.VALIDATION_FAILED.label);
            farmersPayResponse.setMessage("Validation of farmer form failed");
            return ResponseEntity.badRequest().body(farmersPayResponse);
        }

        Farm farm = farmService.getFarmById(updateFarmStatusRequest.getFarmId());
        farm.setFarmStatus(FarmStatus.ACTIVE);
        farmService.save(farm);

        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
        farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
        farmersPayResponse.setResponseData(farm);
        farmersPayResponse.setMessage("The selected farm status has been updated successfully");
        return ResponseEntity.badRequest().body(farmersPayResponse);
    }



    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer <Token>")
    @ApiOperation(value = "Delete farmers farm", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/delete-farm/{farmId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteFarm(
            @PathVariable(required = true) Long farmId,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User authenticatedUser = jwtTokenUtil.getUserFromToken(request);


        Farm farm = farmService.getFarmById(farmId);
        farm.setDeletedAt(LocalDateTime.now());
        farm = farmService.save(farm);

        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
        farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
        farmersPayResponse.setResponseData(farm);
        farmersPayResponse.setMessage("Farm deleted successfully");
        return ResponseEntity.badRequest().body(farmersPayResponse);
    }


    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer <Token>")
    @ApiOperation(value = "List Farms", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/list-farms", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getFarmList(BindingResult bindingResult,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {

        User authenticatedUser = jwtTokenUtil.getUserFromToken(request);

        if (bindingResult.hasErrors()) {
            List errorMessageList =  bindingResult.getFieldErrors().stream().map(fe -> {
                return new ErrorMessage(fe.getField(), fe.getDefaultMessage());
            }).collect(Collectors.toList());

            FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
            farmersPayResponse.setResponseData(errorMessageList);
            farmersPayResponse.setResponseCode(FarmersPayResponseCode.VALIDATION_FAILED.label);
            farmersPayResponse.setMessage("Validation of farmer form failed");
            return ResponseEntity.badRequest().body(farmersPayResponse);
        }


        Long userId = null;
        List<Farm> farmList = new ArrayList<Farm>();

        if(authenticatedUser.getUserRole().equals(UserRole.FARMER))
        {
            userId = authenticatedUser.getId();
            farmList = farmService.getFarmsByUserId(userId);
        }
        else {
            farmList = farmService.getAllFarms();
        }



        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
        farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
        farmersPayResponse.setResponseData(farmList);
        farmersPayResponse.setMessage("Farm list fetched");
        return ResponseEntity.badRequest().body(farmersPayResponse);
    }


    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer <Token>")
    @ApiOperation(value = "Get Farm Details", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/get-farm/{farmId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getFarmList(
                                        @PathVariable(required = true) Long farmId,
                                        BindingResult bindingResult,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        User authenticatedUser = jwtTokenUtil.getUserFromToken(request);


        Farm farm = farmService.getFarmById(farmId);



        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
        farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
        farmersPayResponse.setResponseData(farm);
        farmersPayResponse.setMessage("Farm details fetched");
        return ResponseEntity.badRequest().body(farmersPayResponse);
    }




    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer <Token>")
    @ApiOperation(value = "Add farm bank account", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/add-new-farm-bank-account", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addNewFarmBankAccount(@RequestBody @Valid AddFarmerBankAccountRequest addFarmerBankAccountRequest, BindingResult bindingResult,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {

        User authenticatedUser = jwtTokenUtil.getUserFromToken(request);

        if (bindingResult.hasErrors()) {
            List errorMessageList =  bindingResult.getFieldErrors().stream().map(fe -> {
                return new ErrorMessage(fe.getField(), fe.getDefaultMessage());
            }).collect(Collectors.toList());

            FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
            farmersPayResponse.setResponseData(errorMessageList);
            farmersPayResponse.setResponseCode(FarmersPayResponseCode.VALIDATION_FAILED.label);
            farmersPayResponse.setMessage("Validation of farmer form failed");
            return ResponseEntity.badRequest().body(farmersPayResponse);
        }

        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();

        FarmBankAccount farmBankAccount = farmService.addNewFarmBankAccount(addFarmerBankAccountRequest, authenticatedUser);
        if(farmBankAccount!=null)
        {
            farmersPayResponse.setResponseCode(FarmersPayResponseCode.PROCESS_FAILED.label);
            farmersPayResponse.setMessage("A new bank account could not be added to your farmers profile. Please try again");
            return ResponseEntity.badRequest().body(farmersPayResponse);
        }
        farmersPayResponse.setResponseData(farmBankAccount);
        farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
        farmersPayResponse.setMessage("New bank account added successfully to your farmers profile");
        return ResponseEntity.ok().body(farmersPayResponse);
    }


    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer <Token>")
    @ApiOperation(value = "Delete farms' bank accounts", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/delete-farm-bank-account/{farmId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateFarmBankAccount(@PathVariable Long farmId, BindingResult bindingResult,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws Exception {

        User authenticatedUser = jwtTokenUtil.getUserFromToken(request);


        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();

        List<FarmBankAccount> farmBankAccountList = farmService.getFarmBankAccountsByFarmIdAndUserId(farmId, authenticatedUser.getId());

        farmBankAccountList.stream().map(farmBankAccount -> {
            farmService.deleteFarmBankAccount(farmBankAccount);
            return null;
        });

        farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
        farmersPayResponse.setMessage("Your farm's bank accounts have been deleted");
        return ResponseEntity.ok().body(farmersPayResponse);
    }




    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer <Token>")
    @ApiOperation(value = "Update farm bank account status", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/update-farm-bank-account-status", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateFarmBankAccountStatus(@RequestBody @Valid UpdateFarmerBankAccountStatusRequest updateFarmerBankAccountStatusRequest, BindingResult bindingResult,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) throws Exception {

        User authenticatedUser = jwtTokenUtil.getUserFromToken(request);

        if (bindingResult.hasErrors()) {
            List errorMessageList =  bindingResult.getFieldErrors().stream().map(fe -> {
                return new ErrorMessage(fe.getField(), fe.getDefaultMessage());
            }).collect(Collectors.toList());

            FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
            farmersPayResponse.setResponseData(errorMessageList);
            farmersPayResponse.setResponseCode(FarmersPayResponseCode.VALIDATION_FAILED.label);
            farmersPayResponse.setMessage("Validation of farmer form failed");
            return ResponseEntity.badRequest().body(farmersPayResponse);
        }

        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();

        FarmBankAccount farmBankAccount = farmService.updateFarmBankAccountStatus(updateFarmerBankAccountStatusRequest);
        if(farmBankAccount!=null)
        {
            farmersPayResponse.setResponseCode(FarmersPayResponseCode.PROCESS_FAILED.label);
            farmersPayResponse.setMessage("Your farm bank account status could not be updated. Please try again");
            return ResponseEntity.badRequest().body(farmersPayResponse);
        }
        farmersPayResponse.setResponseData(farmBankAccount);
        farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
        farmersPayResponse.setMessage("Your farm bank account status has been updated successfully");
        return ResponseEntity.ok().body(farmersPayResponse);
    }


    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer <Token>")
    @ApiOperation(value = "Get farm bank account", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/get-farm-bank-account/{farmId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getFarmBankAccount(@PathVariable(required = false) Long farmId,
                                                BindingResult bindingResult,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws Exception {

        User authenticatedUser = jwtTokenUtil.getUserFromToken(request);

        if (bindingResult.hasErrors()) {
            List errorMessageList =  bindingResult.getFieldErrors().stream().map(fe -> {
                return new ErrorMessage(fe.getField(), fe.getDefaultMessage());
            }).collect(Collectors.toList());

            FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
            farmersPayResponse.setResponseData(errorMessageList);
            farmersPayResponse.setResponseCode(FarmersPayResponseCode.VALIDATION_FAILED.label);
            farmersPayResponse.setMessage("Validation of farmer form failed");
            return ResponseEntity.badRequest().body(farmersPayResponse);
        }

        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();

        List<FarmBankAccount> farmBankAccountList = null;

        if(farmId==null && authenticatedUser.getUserRole().equals(UserRole.ADMINISTRATOR))
            farmBankAccountList = farmService.getAllFarmBankAccounts();
        else if(farmId!=null && authenticatedUser.getUserRole().equals(UserRole.ADMINISTRATOR))
            farmBankAccountList = farmService.getFarmBankAccountsByFarmId(farmId);
        else if(farmId!=null && authenticatedUser.getUserRole().equals(UserRole.FARMER))
            farmBankAccountList = farmService.getFarmBankAccountsByFarmIdAndUserId(farmId, authenticatedUser.getId());

        if(farmBankAccountList!=null)
        {
            farmersPayResponse.setResponseCode(FarmersPayResponseCode.PROCESS_FAILED.label);

            farmersPayResponse.setMessage(authenticatedUser.getUserRole().equals(UserRole.ADMINISTRATOR) ?
                    "Bank accounts could not be fetched at this time" :
                    "Your farms bank accounts could not be fetched at this time");
            return ResponseEntity.badRequest().body(farmersPayResponse);
        }
        farmersPayResponse.setResponseData(farmBankAccountList);
        farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
        farmersPayResponse.setMessage(authenticatedUser.getUserRole().equals(UserRole.ADMINISTRATOR) ?
                "Bank accounts fetched successfully" :
                "Your farm bank accounts fetched successfully");
        return ResponseEntity.ok().body(farmersPayResponse);
    }





}
