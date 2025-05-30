package com.fitnessStore.backend.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnessStore.backend.ExceptionHandling.IncorrectToken;
import com.fitnessStore.backend.apiServices.JWTServices;
import com.fitnessStore.backend.apiServices.MyUserDetailsServices;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTServices jwtServices;

    @Autowired
    ApplicationContext context;

    private final ObjectMapper objectMapper=new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if ("/api/user/signUp".equals(path) || "/api/user/login".equals(path) || "/api/user/logout".equals(path) /*|| "api/auth/check-auth".equals(path) || "api/admin/product/upload-image".equals(path)*/) {
            filterChain.doFilter(request, response);
            return;
        }
        try {

            String token = null;
            String email = null;

            if(request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("token".equals(cookie.getName())) {
                        token = cookie.getValue();
                        email = jwtServices.extractEmail(token);
                        break;
                    }
                }
            }

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = context.getBean(MyUserDetailsServices.class).loadUserByUsername(email);

                if (jwtServices.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        }
        catch (IncorrectToken e){
            handleException(response,e.getMessage());
        }

    }

    private void handleException(HttpServletResponse response,String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        Map<String,String> errorResponse = new HashMap<>();
        errorResponse.put("status","401");
        errorResponse.put("error","unauthorized");
        errorResponse.put("message",message);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
