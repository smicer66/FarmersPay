package com.probase.fra.farmerspay.api.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.probase.fra.farmerspay.api.models.User;
import com.probase.fra.farmerspay.api.models.UserRolePermission;
import com.probase.fra.farmerspay.api.models.responses.FarmersPayResponse;
import com.probase.fra.farmerspay.api.providers.TokenProvider;
import com.probase.fra.farmerspay.api.service.UserService;
import com.probase.fra.farmerspay.api.enums.FarmersPayResponseCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isEmpty;


//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class JwtTokenFilter extends OncePerRequestFilter {

    private final TokenProvider jwtTokenUtil;

    private final UserService userService;

    public JwtTokenFilter(TokenProvider jwtTokenUtil, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException, IOException {
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        final String token = header.split(" ")[1].trim();
        try {
            if (!jwtTokenUtil.validateToken(token)) {
                chain.doFilter(request, response);
                return;


            }
        }
        catch(ExpiredJwtException e)
        {
            logger.info("Exception occured");
            Map<String, String> errors = new HashMap<>();
            FarmersPayResponse payAccessResponse = new FarmersPayResponse();
            payAccessResponse.setResponseCode(FarmersPayResponseCode.EXPIRED_TOKEN.label);
            payAccessResponse.setMessage("Token expired. Please provide a new token");
            payAccessResponse.setResponseData(errors);

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
//            response.getWriter().write(new ObjectMapper().writeValueAsString(payAccessResponse));
            out.print(new ObjectMapper().writeValueAsString(payAccessResponse));
            out.flush();


            return;
        }
        catch(UnsupportedJwtException e)
        {
            e.printStackTrace();
            logger.info("Exception occured");
            Map<String, String> errors = new HashMap<>();
            FarmersPayResponse payAccessResponse = new FarmersPayResponse();
            payAccessResponse.setResponseCode(FarmersPayResponseCode.INVALID_AUTH_TOKEN.label);
            payAccessResponse.setMessage("Invalid token identifier. Please provide a valid authentication token");
            payAccessResponse.setResponseData(errors);

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.getWriter().write(new ObjectMapper().writeValueAsString(payAccessResponse));
            out.print(new ObjectMapper().writeValueAsString(payAccessResponse));
            out.flush();


            return;
        }

        String username = jwtTokenUtil.getUsernameFromToken(token);
        logger.info(username);
        User user = userService.findByUsername(username);
        logger.info(user.getUserRole().name());
        List<UserRolePermission> userRolePermissionList = userService.getPermissionsByRole(user.getUserRole(), 0, Integer.MAX_VALUE);
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        Set authList = new HashSet();
        if(userRolePermissionList!=null && !userRolePermissionList.isEmpty())
        {
            authList = userRolePermissionList.stream().map(urp -> {
                return new SimpleGrantedAuthority("ROLE_" +urp.getPermission().name());
            }).collect(Collectors.toSet());
        }


        logger.info(authList);

//        jsonObject.put("permissions", userRolePermissionList);


        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(), null, authList
        );

        logger.info(authentication.isAuthenticated());

        logger.info(authentication);

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        logger.info(2);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info(3);
        chain.doFilter(request, response);
        logger.info(request.getParameter("userRole"));
    }

}