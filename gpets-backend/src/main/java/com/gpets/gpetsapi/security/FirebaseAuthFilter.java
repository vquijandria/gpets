package com.gpets.gpetsapi.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class FirebaseAuthFilter extends OncePerRequestFilter {

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/public/",
            "/swagger-ui",
            "/v3/api-docs"
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // ✅ CORS preflight
        if ("OPTIONS".equalsIgnoreCase(method)) {
            chain.doFilter(request, response);
            return;
        }

        // ✅ DEBUG (temporal)
        String authHeader = request.getHeader("Authorization");
        System.out.println("FirebaseAuthFilter -> " + method + " " + path);
        System.out.println("Authorization header present? " + (authHeader != null));

        // ✅ Swagger y rutas públicas sin auth
        if (PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
            chain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"missing_bearer_token\"}");
            return;
        }

        String idToken = authHeader.substring("Bearer ".length()).trim();

        try {
            FirebaseToken decoded = FirebaseAuth.getInstance().verifyIdToken(idToken);

            var authentication = new UsernamePasswordAuthenticationToken(
                    decoded.getUid(),
                    null,
                    List.of()
            );

            SecurityContextHolder.clearContext();
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 👇 atributos útiles para controllers
            request.setAttribute("uid", decoded.getUid());
            request.setAttribute("email", decoded.getEmail());
            request.setAttribute("name", decoded.getName());

            chain.doFilter(request, response);

        } catch (Exception ex) {
            // ✅ DEBUG (temporal)
            ex.printStackTrace();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"invalid_or_expired_token\"}");
        }
    }
}
