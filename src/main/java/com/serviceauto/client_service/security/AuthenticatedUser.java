package com.serviceauto.client_service.security;

public record AuthenticatedUser(
        Long userId,
        String username,
        String role
) {}
