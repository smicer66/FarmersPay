package com.probase.fra.farmerspay.api.controllers;


import com.probase.fra.farmerspay.api.enums.FarmersPayResponseCode;
import com.probase.fra.farmerspay.api.models.ErrorMessage;
import com.probase.fra.farmerspay.api.models.User;
import com.probase.fra.farmerspay.api.models.WorkflowUser;
import com.probase.fra.farmerspay.api.models.requests.AddUserTypeRequest;
import com.probase.fra.farmerspay.api.models.requests.BulkPaymentRequest;
import com.probase.fra.farmerspay.api.models.responses.FarmersPayResponse;
import com.probase.fra.farmerspay.api.providers.TokenProvider;
import com.probase.fra.farmerspay.api.service.FarmService;
import com.probase.fra.farmerspay.api.service.UserService;
import com.probase.fra.farmerspay.api.service.WorkflowService;
import io.swagger.annotations.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.probase.fra.farmerspay.api.models.requests.CreateWorkflowRequest;

@RestController
@RequestMapping("/api/v1/workflow")
@Api(produces = "application/json", description = "Workflow management")
public class WorkflowController {

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider jwtTokenUtil;


    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer <Token>")
    @ApiOperation(value = "Get Workflow Users", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/get-workflow-users/{pageSize}/{pageNumber}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getWorkflowUsers(
            @PathVariable Integer pageSize,
            @PathVariable Integer pageNumber,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User authenticatedUser = jwtTokenUtil.getUserFromToken(request);


        Map map = workflowService.getAllWorkflowUsers(pageNumber, pageSize);

        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
        if(!map.isEmpty())
        {
            farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
            farmersPayResponse.setResponseData(map);
            farmersPayResponse.setMessage("Workflow listing fetched");
            return ResponseEntity.ok().body(farmersPayResponse);
        }

        farmersPayResponse.setResponseCode(FarmersPayResponseCode.GENERAL_ERROR.label);
        farmersPayResponse.setResponseData(null);
        farmersPayResponse.setMessage("Workflow listing could not be fetched");
        return ResponseEntity.badRequest().body(farmersPayResponse);
    }


    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer <Token>")
    @ApiOperation(value = "Create Workflow Users", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/create-workflow", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createWorkflow(
            @RequestBody @Valid CreateWorkflowRequest createWorkflowRequest, BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User authenticatedUser = jwtTokenUtil.getUserFromToken(request);


        List<WorkflowUser> workflowUserList = workflowService.createWorkflow(createWorkflowRequest, userService);

        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
        if(!workflowUserList.isEmpty())
        {
            farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
            farmersPayResponse.setResponseData(workflowUserList);
            farmersPayResponse.setMessage("Workflow has been created successfully");
            return ResponseEntity.ok().body(farmersPayResponse);
        }

        farmersPayResponse.setResponseCode(FarmersPayResponseCode.GENERAL_ERROR.label);
        farmersPayResponse.setResponseData(null);
        farmersPayResponse.setMessage("Workflow has not been created successfully");
        return ResponseEntity.badRequest().body(farmersPayResponse);
    }
}
