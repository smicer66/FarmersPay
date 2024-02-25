package com.probase.fra.farmerspay.api.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.probase.fra.farmerspay.api.enums.FarmersPayResponseCode;
import com.probase.fra.farmerspay.api.models.Farm;
import com.probase.fra.farmerspay.api.models.FarmDTO;
import com.probase.fra.farmerspay.api.models.User;
import com.probase.fra.farmerspay.api.models.requests.LoginRequest;
import com.probase.fra.farmerspay.api.models.responses.AuthenticateResponse;
import com.probase.fra.farmerspay.api.models.responses.FarmersPayResponse;
import com.probase.fra.farmerspay.api.providers.TokenProvider;
import com.probase.fra.farmerspay.api.service.FarmService;
import com.probase.fra.farmerspay.api.service.UserService;
import com.probase.fra.farmerspay.util.UtilityHelper;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Value("${key.decrypt.key}")
    private String decryptKey;


    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AuthenticationManager authenticationManager;



    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody LoginRequest loginUser) throws AuthenticationException, JsonProcessingException {

        try {
            try {
//                String pss = UtilityHelper.encryptData("passwordzaq1ZAQ!", decryptKey);
//                logger.info("pss ... {}", pss);
//                String password = UtilityHelper.decryptData(pss, decryptKey).toString();
//                logger.info("password ... {}", password);
                logger.info("pass ...{}", loginUser.getPassword());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            String password = UtilityHelper.decryptData(loginUser.getPassword(), decryptKey).toString();

            final Authentication authentication = authenticationManager.authenticate(

                    new UsernamePasswordAuthenticationToken(
                            loginUser.getUsername(),
                            password
//                            loginUser.getPassword()
                    )
            );

            logger.info("{}", authentication.isAuthenticated());
            logger.info("{}", authentication.getPrincipal());
            //        logger.info("{}>>>>", loginUser.getEmailAddress());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = jwtTokenUtil.generateToken(authentication);
            logger.info("token...{}", token);


            User loggedInUser = (User)authentication.getPrincipal();


            Map farmList = farmService.getFarmsByUserId(null, loggedInUser.getId(), Integer.MAX_VALUE, 0);
            AuthenticateResponse authenticateResponse = new AuthenticateResponse();
            authenticateResponse.setToken(token);
            authenticateResponse.setUserRole(loggedInUser.getUserRole());
            authenticateResponse.setStatus(FarmersPayResponseCode.SUCCESS);
            authenticateResponse.setFarmList(farmList);
            authenticateResponse.setUser(loggedInUser);
            authenticateResponse.setMessage("Login successful");


            return ResponseEntity.ok(authenticateResponse);
        }
        catch(ProviderNotFoundException e)
        {
            Map farmList = new HashMap();
            AuthenticateResponse authenticateResponse = new AuthenticateResponse();
            authenticateResponse.setToken(null);
            authenticateResponse.setMessage("Invalid username/password combination. Please provide a valid username/password to log in");
            authenticateResponse.setFarmList(farmList);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authenticateResponse);
        }
    }



}
