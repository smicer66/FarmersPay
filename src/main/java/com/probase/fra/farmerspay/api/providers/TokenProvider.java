package com.probase.fra.farmerspay.api.providers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.probase.fra.farmerspay.api.exceptions.FarmersPayAuthException;
import com.probase.fra.farmerspay.api.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;


@Component
public class TokenProvider implements Serializable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());



    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("${jwt.signing.key}")
    private String secret;

    @Value("${jwt.authorities.key}")
    private String roleKey;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(Authentication userDetails) throws JsonProcessingException {
        Map<String, Object> claims = new HashMap<>();
        claims.put(roleKey, userDetails.getAuthorities());
//        claims.put("username", ((User)(userDetails.getPrincipal())).getEmailAddress());
        claims.put("user", userDetails.getPrincipal());
        logger.info("princ...{}",  ((User)userDetails.getPrincipal()).getCreatedAt());
        return doGenerateToken(claims, ((User)(userDetails.getPrincipal())).getUsername());
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
//                .registerModule(javaTimeModule)
                .registerModule(new JavaTimeModule());

        logger.info("subect...{}", subject);




        return Jwts.builder()
//                .serializeToJsonWith(new JacksonDeserializer(objectMapper))
                .setClaims(claims).
                setSubject(subject).
                setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    //validate token
    public Boolean validateToken(String token
//            , UserDetails userDetails
    ) {
//        final String username = getUsernameFromToken(token);
        return
//                (username.equals(userDetails.getUsername()) &&
                !isTokenExpired(token);
//        );
    }

    public User getUserFromToken(String token) throws FarmersPayAuthException {
//        Claims c = Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token).getBody();
//        return c.getSubject();
//        return getClaimFromToken(token, Claims::getSubject);
//        Jwts.builder().claims().
        try
        {
            Claims c = Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token).getBody();
            logger.info("....{}", c.get("user"));
            LinkedHashMap lhm = (LinkedHashMap)(c.get("user"));
            logger.info("{}", lhm);
            logger.info("{}", lhm.get("id"));

            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.convertValue(lhm, User.class);

            return user;
        }
        catch (UnsupportedJwtException e)
        {
            throw new FarmersPayAuthException("Authorization failed");
        }

    }


    public User getUserFromToken(HttpServletRequest request) throws JsonProcessingException, FarmersPayAuthException {
        String token = null;
        Enumeration<String> headers = request.getHeaderNames();
        while(headers.hasMoreElements()) {
            String key = headers.nextElement();
            if(key.trim().equalsIgnoreCase("Authorization")) {
                String authorizationHeader = request.getHeader(key);
                if(!authorizationHeader.isEmpty()) {
                    String[] tokenData = authorizationHeader.split(" ");
                    if(tokenData.length == 2 && tokenData[0].trim().equalsIgnoreCase("Bearer")) {
                        token = tokenData[1];
                        logger.info("Received token: " + token);
                        break;
                    }
                }
            }
        }

        User authenticatedUser = this.getUserFromToken(token);
        logger.info("authenticatedUser.....{}" + authenticatedUser);
        return authenticatedUser;
    }
}
