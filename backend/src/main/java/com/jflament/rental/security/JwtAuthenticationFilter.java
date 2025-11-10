package com.jflament.rental.security;

import com.jflament.rental.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;

    public JwtAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // pas de token → laisser le contrôleur gérer
            return;
        }

        String token = authHeader.substring(7);
        String email = JwtUtil.extractEmail(token);

        if (email == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            return;
        }

        // vérifier si l'utilisateur est déjà authentifié
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            userService.findByEmail(email).ifPresentOrElse(user -> {
                try {
                    if (!JwtUtil.validateToken(token)) { // <-- juste un boolean
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                        return;
                    }

                    // créer un UserDetails et remplir le SecurityContext
                    CustomUserDetails userDetails = new CustomUserDetails(user);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, () -> {
                try {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        filterChain.doFilter(request, response); // continuer vers le contrôleur
    }
}
