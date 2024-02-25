package com.probase.fra.farmerspay.api.controllers;


import com.probase.fra.farmerspay.api.enums.FarmStatus;
import com.probase.fra.farmerspay.api.enums.FarmersPayResponseCode;
import com.probase.fra.farmerspay.api.models.*;
import com.probase.fra.farmerspay.api.models.requests.NewFarmRequest;
import com.probase.fra.farmerspay.api.models.responses.FarmersPayResponse;
import com.probase.fra.farmerspay.api.providers.TokenProvider;
import com.probase.fra.farmerspay.api.service.FarmService;
import com.probase.fra.farmerspay.api.service.UtilityService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/utilities")
@Api(produces = "application/json", description = "Farmer management")
public class UtilityController {


    @Autowired
    private FarmService farmService;
    @Autowired
    private UtilityService utilityService;
    @Autowired
    private TokenProvider jwtTokenUtil;



    @ApiOperation(value = "Fetch default data", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/fetch-default-data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity fetchDefaultData(HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {




        List<District> districtList = utilityService.fetchDistricts();
        List<Province> provinceList = utilityService.fetchProvinces();


        Map<String, List> defaultData = new HashMap<String, List>();
        defaultData.put("provinceList", provinceList);
        defaultData.put("districtList", districtList);

        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
        farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
        farmersPayResponse.setResponseData(defaultData);
        farmersPayResponse.setMessage("Default data fetched");
        return ResponseEntity.ok().body(farmersPayResponse);
    }



    @ApiOperation(value = "Fetch districts by province Id", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 400, message = "Validation of request parameters failed"),
            @ApiResponse(code = 403, message = "Access to API denied due to invalid token"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    @RequestMapping(value="/fetch-districts-by-province/{provinceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity fetchDefaultData(
                                            @PathVariable(required = true) Long provinceId,
                                            HttpServletRequest request,
                                            HttpServletResponse response) throws Exception {




        List<District> districtList = utilityService.fetchDistrictByProvinceId(provinceId);

        FarmersPayResponse farmersPayResponse = new FarmersPayResponse();
        farmersPayResponse.setResponseCode(FarmersPayResponseCode.SUCCESS.label);
        farmersPayResponse.setResponseData(districtList);
        farmersPayResponse.setMessage("Districts fetched");
        return ResponseEntity.badRequest().body(farmersPayResponse);
    }

}
