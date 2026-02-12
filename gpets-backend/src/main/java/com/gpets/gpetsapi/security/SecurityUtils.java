package com.gpets.gpetsapi.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static AuthUser currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new RuntimeException("Unauthenticated");
        }
        String uid = auth.getPrincipal().toString();
        String email = auth.getCredentials() == null ? null : auth.getCredentials().toString();
        return new AuthUser(uid, email);
    }
}
