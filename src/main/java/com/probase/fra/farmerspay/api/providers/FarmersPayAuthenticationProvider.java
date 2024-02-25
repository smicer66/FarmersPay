package com.probase.fra.farmerspay.api.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.probase.fra.farmerspay.api.enums.FarmersPayResponseCode;
import com.probase.fra.farmerspay.api.enums.UserRole;
import com.probase.fra.farmerspay.api.exceptions.FarmersPayAuthException;
import com.probase.fra.farmerspay.api.models.Farm;
import com.probase.fra.farmerspay.api.models.User;
import com.probase.fra.farmerspay.api.models.responses.AuthenticateResponse;
import com.probase.fra.farmerspay.api.service.UserService;
import lombok.SneakyThrows;
import net.minidev.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;


@Component
public class FarmersPayAuthenticationProvider implements AuthenticationProvider {

    public FarmersPayAuthenticationProvider() {
        super();
    }
    private Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Autowired
//    private RestTemplate restTemplate;
    @Autowired
    UserService userService;

    @SneakyThrows
    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

//        JavaTimeModule javaTimeModule = new JavaTimeModule();
//        javaTimeModule.addDeserializer(LocalDateTime.class, new TimestampDeserializer());
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
//                .registerModule(javaTimeModule)
                .registerModule(new JavaTimeModule());


        String username = authentication.getPrincipal().toString(); // (1)

        logger.info("{} ... {}", username, authentication);
        String password = authentication.getCredentials().toString(); // (1)

        logger.info("{} ... {}", username, password);





        try
        {
            logger.info("username ... {}", username);
            User user = userService.findByUsername(username);

            if(user==null)
            {
                throw new FarmersPayAuthException("Invalid username/password combination. Please provide a valid username/password combination");
            }

            logger.info("user id ... {}", user.getId());
            boolean matchedPin = BCrypt.checkpw(password, user.getPassword());

            if(matchedPin==false)
            {
                throw new FarmersPayAuthException("Invalid username/password combination. Please provide a valid username/password combination");
            }

//            List farmList = new ArrayList<Farm>();
//            AuthenticateResponse authenticateResponse = new AuthenticateResponse();
//            authenticateResponse.setFarmList(farmList);
//            authenticateResponse.setStatus(Integer.valueOf(FarmersPayResponseCode.AUTH_FAIL.label));
//            authenticateResponse.setUserRole(user.getUserRole());
//            authenticateResponse.setMessage();

            UserRole userRole = user.getUserRole();
            logger.info("use id .. {}", user.getId());

            Set<SimpleGrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.name()));


            UsernamePasswordAuthenticationToken r = new UsernamePasswordAuthenticationToken(user, password, authorities); // (4)
            logger.info("{}", r);
            logger.info("{}", r.isAuthenticated());

            return r;
//            ResponseEntity<AuthenticateResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, entity, AuthenticateResponse.class);
//            if(responseEntity.getStatusCode().equals(HttpStatus.OK))
//            {
//                AuthenticateResponse authenticateResponse = responseEntity.getBody();
//
//                if(authenticateResponse!=null && authenticateResponse.getStatus().intValue()==1)
//                {
//                    throw new FarmersPayAuthException(authenticateResponse.getMessage());
//                }
//                logger.info("{}", authenticateResponse);
//                logger.info("{} merchantList", authenticateResponse.getFarmList());
////        logger.info("iswAuthTokenResponse...{}", payAccessAuthResponse.getAccess_token());
//                HttpStatus httpStatus = responseEntity.getStatusCode();
//
////        User user = payAccessAuthResponse.getAuthenticatedUser();
//
//            }
//            else if(responseEntity.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
//            {
//                throw new FarmersPayAuthException("Invalid credentials");
//            }

//            responseEntity.g

        }
        catch(HttpServerErrorException e)
        {
            logger.info("HttpServerErrorException ... {}", e.getResponseBodyAsString());
            throw new Exception("Connection to authentication server timed out");

        } catch (HttpClientErrorException e) {
            logger.info("{}", e.getResponseBodyAsString());
            throw new Exception("Connection to authentication server timed out");
        } catch (ResourceAccessException e) {
            logger.info("ResourceAccessException ... {}", e.getMessage());
            throw new TimeoutException("Connection to resource timed out");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        logger.info("{}", authentication.getCanonicalName());
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
