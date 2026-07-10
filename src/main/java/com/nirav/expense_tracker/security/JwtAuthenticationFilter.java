package com.nirav.expense_tracker.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nirav.expense_tracker.dto.response.ApiResponse;
import com.nirav.expense_tracker.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String token = getJwtFromRequest(request);

            if (token != null) {
                String username = jwtUtils.extractUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtUtils.validateToken(token, userDetails)) {

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        authentication.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }

            filterChain.doFilter(request, response);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("JWT Token expired: " + e.getMessage());
            handleAuthenticationError(response, "Token has expired. Please login again.");

        } catch (io.jsonwebtoken.MalformedJwtException e) {
            System.out.println("Malformed JWT token: " + e.getMessage());
            handleAuthenticationError(response, "Invalid token format. Please login again.");

        } catch (io.jsonwebtoken.SignatureException e) {
            System.out.println("JWT signature validation failed: " + e.getMessage());
            handleAuthenticationError(response, "Invalid token signature. Please login again.");

        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token: " + e.getMessage());
            handleAuthenticationError(response, "Unsupported token format. Please login again.");

        } catch (io.jsonwebtoken.JwtException e) {
            System.out.println("JWT exception occurred: " + e.getMessage());
            handleAuthenticationError(response, "Invalid token. Please login again.");

        } catch (org.springframework.security.core.userdetails.UsernameNotFoundException e) {
            System.out.println("User not found: " + e.getMessage());
            handleAuthenticationError(response, "User not found. Please login again.");

        } catch (Exception e) {
            System.out.println("Unexpected error in JWT filter: " + e.getMessage());
            e.printStackTrace();
            handleAuthenticationError(response, "Authentication failed. Please try again.");
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void handleAuthenticationError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        ApiResponse<Object> apiResponse = ApiResponse.error(message);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        response.getWriter().write(mapper.writeValueAsString(apiResponse));
        response.getWriter().flush();
    }
}