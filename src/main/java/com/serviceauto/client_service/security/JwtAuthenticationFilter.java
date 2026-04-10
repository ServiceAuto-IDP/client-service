package com.serviceauto.client_service.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        AuthenticatedUser authenticatedUser = decodeToken(authorizationHeader.substring(7));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticatedUser,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + authenticatedUser.role()))
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private AuthenticatedUser decodeToken(String token) {
        try {
            String[] chunks = token.split("\\.");
            if (chunks.length < 2) {
                throw new BadCredentialsException("Invalid JWT token format");
            }

            byte[] payload = Base64.getUrlDecoder().decode(chunks[1]);
            Map<String, Object> claims = objectMapper.readValue(payload, new TypeReference<>() {
            });

            Object userIdClaim = claims.get("userId");
            if (userIdClaim == null) {
                throw new BadCredentialsException("JWT token does not contain userId");
            }

            Long userId = Long.valueOf(String.valueOf(userIdClaim));
            String email = String.valueOf(claims.getOrDefault("sub", ""));
            String role = String.valueOf(claims.getOrDefault("role", "CLIENT"));
            return new AuthenticatedUser(userId, email, role);
        } catch (IllegalArgumentException exception) {
            throw new BadCredentialsException("Invalid JWT token", exception);
        } catch (IOException exception) {
            throw new BadCredentialsException("Unable to parse JWT token", exception);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator") || path.equals("/api/client/ping");
    }
}
