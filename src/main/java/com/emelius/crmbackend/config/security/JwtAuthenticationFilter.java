package com.emelius.crmbackend.config.security;

import com.emelius.crmbackend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Si no hay token, dejamos que siga (Spring Security lo bloqueará después si la ruta es privada)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extraemos el token (quitando la palabra "Bearer ")
        jwt = authHeader.substring(7);

        try {
            userEmail = jwtService.extractUsername(jwt);

            // 3. Si hay un correo y aún no está autenticado en este hilo
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 4. Validamos el token
                if (jwtService.isTokenValid(jwt)) {
                    // 5. ¡Le decimos a Spring Security que este usuario tiene permiso de pasar!
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userEmail, null, new ArrayList<>() // Aquí irían los roles en el futuro
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Si el token es inválido, simplemente no lo autenticamos y Spring dará el 403
            System.out.println("Token inválido o expirado");
        }

        filterChain.doFilter(request, response);
    }
}