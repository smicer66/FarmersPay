package com.probase.fra.farmerspay.api.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.probase.fra.farmerspay.api.enums.FarmersPayResponseCode;
import com.probase.fra.farmerspay.api.models.Farm;
import com.probase.fra.farmerspay.api.models.User;
import com.probase.fra.farmerspay.api.models.requests.LoginRequest;
import com.probase.fra.farmerspay.api.models.responses.AuthenticateResponse;
import com.probase.fra.farmerspay.api.models.responses.FarmersPayResponse;
import com.probase.fra.farmerspay.api.providers.TokenProvider;
import com.probase.fra.farmerspay.api.service.FarmService;
import com.probase.fra.farmerspay.api.service.UserService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@Api(produces = "application/json", description = "Operations pertaining to Authentication. Ignore for APIs on Authentication Server")
public class AuthenticationController {


    @Autowired
    UserService userService;
    @Autowired
    FarmService farmService;
    @Autowired
    private TokenProvider jwtTokenUtil;


    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AuthenticationManager authenticationManager;



    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody LoginRequest loginUser) throws AuthenticationException, JsonProcessingException {

        try {
            final Authentication authentication = authenticationManager.authenticate(

                    new UsernamePasswordAuthenticationToken(
                            loginUser.getUsername(),
                            loginUser.getPassword()
                    )
            );

            logger.info("{}", authentication.isAuthenticated());
            logger.info("{}", authentication.getPrincipal());
            //        logger.info("{}>>>>", loginUser.getEmailAddress());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = jwtTokenUtil.generateToken(authentication);
            logger.info("token...{}", token);


            User loggedInUser = (User)authentication.getPrincipal();


            List<Farm> farmList = farmService.getFarmsByUserId(loggedInUser.getId());
            AuthenticateResponse authenticateResponse = new AuthenticateResponse();
            authenticateResponse.setToken(token);
            authenticateResponse.setUserRole(loggedInUser.getUserRole());
            authenticateResponse.setStatus(FarmersPayResponseCode.SUCCESS);
            authenticateResponse.setFarmList(farmList);
            authenticateResponse.setMessage("Login successful");


            return ResponseEntity.ok(authenticateResponse);
        }
        catch(ProviderNotFoundException e)
        {
            List farmList = new ArrayList<Farm>();
            AuthenticateResponse authenticateResponse = new AuthenticateResponse();
            authenticateResponse.setToken(null);
            authenticateResponse.setMessage("Invalid username/password combination. Please provide a valid username/password to log in");
            authenticateResponse.setFarmList(farmList);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authenticateResponse);
        }
    }



}
