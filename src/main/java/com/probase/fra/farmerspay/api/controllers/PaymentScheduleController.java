package com.probase.fra.farmerspay.api.controllers;


import com.probase.fra.farmerspay.api.enums.FarmersPayResponseCode;
import com.probase.fra.farmerspay.api.models.*;
import com.probase.fra.farmerspay.api.models.requests.CreatePaymentScheduleRequest;
import com.probase.fra.farmerspay.api.models.requests.CreateWorkflowRequest;
import com.probase.fra.farmerspay.api.models.responses.FarmersPayResponse;
import com.probase.fra.farmerspay.api.providers.TokenProvider;
import com.probase.fra.farmerspay.api.service.FarmService;
import com.probase.fra.farmerspay.api.service.PaymentScheduleBreakdownService;
import com.probase.fra.farmerspay.api.service.UserService;
import com.probase.fra.farmerspay.api.service.PaymentScheduleService;
import io.swagger.annotations.*;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/api/v1/payment-schedule")
@Api(produces = "application/json", description = "Workflow management")
public class PaymentScheduleController {

    @Autowired
    private PaymentScheduleService paymentScheduleService;

    @Autowired
    private PaymentScheduleBreakdownService paymentScheduleBreakdownService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FarmService farmService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider jwtTokenUtil;


    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer <Token>")
    @ApiOperation(value = "Get Payment Schedules", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/get-payment-schedules/{pageSize}/{pageNumber}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getWorkflowUsers(
            @PathVariable Integer pageSize,
            @PathVariable Integer pageNumber,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User authenticatedUser = jwtTokenUtil.getUserFromToken(request);


        Map map = paymentScheduleService.getAllPaymentSchedules(pageNumber, pageSize);

        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
        if(!map.isEmpty())
        {
            farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
            farmersPayResponse.setResponseData(map);
            farmersPayResponse.setMessage("Payment Schedule listing fetched");
            return ResponseEntity.ok().body(farmersPayResponse);
        }

        farmersPayResponse.setResponseCode(FarmersPayResponseCode.GENERAL_ERROR.label);
        farmersPayResponse.setResponseData(null);
        farmersPayResponse.setMessage("Payment Schedule listing could not be fetched");
        return ResponseEntity.badRequest().body(farmersPayResponse);
    }








    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer <Token>")
    @ApiOperation(value = "Get Payment Schedules", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/get-payment-schedule-breakdown/{paymentScheduleId}/{pageSize}/{pageNumber}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getWorkflowUsers(
            @PathVariable Long paymentScheduleId,
            @PathVariable Integer pageSize,
            @PathVariable Integer pageNumber,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User authenticatedUser = jwtTokenUtil.getUserFromToken(request);

        Map map = paymentScheduleBreakdownService.getPaymentScheduleBreakdownByPayScheduleId(paymentScheduleId, pageNumber, pageSize);

        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
        if(!map.isEmpty())
        {
            farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
            farmersPayResponse.setResponseData(map);
            farmersPayResponse.setMessage("Payment Schedule breakdown listing fetched");
            return ResponseEntity.ok().body(farmersPayResponse);
        }

        farmersPayResponse.setResponseCode(FarmersPayResponseCode.GENERAL_ERROR.label);
        farmersPayResponse.setResponseData(null);
        farmersPayResponse.setMessage("Payment Schedule breakdown listing could not be fetched");
        return ResponseEntity.badRequest().body(farmersPayResponse);
    }


    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer <Token>")
    @ApiOperation(value = "Create Payment Schedule", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/create-payment-schedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createPaymentSchedule(
            @RequestBody @Valid CreatePaymentScheduleRequest createPaymentScheduleRequest, BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User authenticatedUser = jwtTokenUtil.getUserFromToken(request);



        PaymentSchedule paymentSchedule = paymentScheduleService.createPaymentSchedule(createPaymentScheduleRequest, authenticatedUser, farmService);
        paymentScheduleBreakdownService.createPaymentScheduleBreakdown(createPaymentScheduleRequest, paymentSchedule, farmService);


        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
        if(paymentSchedule!=null)
        {
            farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
            farmersPayResponse.setResponseData(paymentSchedule);
            farmersPayResponse.setMessage("Payment schedule has been created successfully");
            return ResponseEntity.ok().body(farmersPayResponse);
        }

        farmersPayResponse.setResponseCode(FarmersPayResponseCode.GENERAL_ERROR.label);
        farmersPayResponse.setResponseData(null);
        farmersPayResponse.setMessage("Payment schedule has not been created successfully");
        return ResponseEntity.badRequest().body(farmersPayResponse);
    }
}
