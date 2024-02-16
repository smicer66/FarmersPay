package com.probase.fra.farmerspay.api.controllers;


import com.probase.fra.farmerspay.api.enums.FarmStatus;
import com.probase.fra.farmerspay.api.enums.FarmersPayResponseCode;
import com.probase.fra.farmerspay.api.enums.UserRole;
import com.probase.fra.farmerspay.api.models.*;
import com.probase.fra.farmerspay.api.models.requests.*;
import com.probase.fra.farmerspay.api.models.responses.FarmersPayResponse;
import com.probase.fra.farmerspay.api.providers.TokenProvider;
import com.probase.fra.farmerspay.api.service.BankService;
import com.probase.fra.farmerspay.api.service.FarmService;
import io.swagger.annotations.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/banks")
@Api(produces = "application/json", description = "Farmer management")
public class BankController {


    @Autowired
    private BankService bankService;
    @Autowired
    private TokenProvider jwtTokenUtil;


    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer <Token>")
    @ApiOperation(value = "Add new bank", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/add-new-bank", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addNewBank(@RequestBody @Valid NewBankRequest newBankRequest, BindingResult bindingResult,
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


        Bank bank = bankService.save(newBankRequest, authenticatedUser);

        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
        farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
        farmersPayResponse.setResponseData(bank);
        farmersPayResponse.setMessage("Bank added successfully");
        return ResponseEntity.badRequest().body(farmersPayResponse);
    }






    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer <Token>")
    @ApiOperation(value = "Update bank", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/update-bank", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateBank(@RequestBody @Valid UpdateBankRequest updateBankRequest, BindingResult bindingResult,
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

        Bank bank = bankService.updateFarmBankAccount(updateBankRequest, authenticatedUser);
        if(bank!=null)
        {
            farmersPayResponse.setResponseCode(FarmersPayResponseCode.PROCESS_FAILED.label);
            farmersPayResponse.setMessage("Bank could not be updated. Please try again");
            return ResponseEntity.badRequest().body(farmersPayResponse);
        }


        farmersPayResponse.setResponseData(bank);
        farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
        farmersPayResponse.setMessage("Bank has been updated successfully");
        return ResponseEntity.ok().body(farmersPayResponse);
    }






    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer <Token>")
    @ApiOperation(value = "Get bank", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/get-bank-by-id/{bankId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getFarmBankAccount(@PathVariable(required = false) Long bankId,
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

        Bank bank = bankService.getBankById(bankId);

        if(bank==null)
        {
            farmersPayResponse.setResponseCode(FarmersPayResponseCode.PROCESS_FAILED.label);

            farmersPayResponse.setMessage("Bank could not be fetched at this time");
            return ResponseEntity.badRequest().body(farmersPayResponse);
        }
        farmersPayResponse.setResponseData(bank);
        farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
        farmersPayResponse.setMessage("Bank fetched successfully");
        return ResponseEntity.ok().body(farmersPayResponse);
    }






    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer <Token>")
    @ApiOperation(value = "Get banks", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/get-banks/{pageNumber}/{pageSize}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getFarmBankAccount(@PathVariable(required = false) Integer pageNumber,
                                             @PathVariable(required = false) Integer pageSize,
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

        List<Bank> bankList = bankService.getAllBanks(pageNumber, pageSize);

        if(bankList==null)
        {
            farmersPayResponse.setResponseCode(FarmersPayResponseCode.PROCESS_FAILED.label);

            farmersPayResponse.setMessage("Bank could not be fetched at this time");
            return ResponseEntity.badRequest().body(farmersPayResponse);
        }
        farmersPayResponse.setResponseData(bankList);
        farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
        farmersPayResponse.setMessage("Banks fetched successfully");
        return ResponseEntity.ok().body(farmersPayResponse);
    }

}
