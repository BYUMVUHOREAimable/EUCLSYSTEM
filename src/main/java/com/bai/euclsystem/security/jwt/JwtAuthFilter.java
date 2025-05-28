package com.bai.euclsystem.security.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.bai.euclsystem.exceptions.JWTVerificationException;
import com.bai.euclsystem.exceptions.TokenException;
import com.bai.euclsystem.security.user.CustomUserDetailsService;
import com.bai.euclsystem.security.user.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    public void throwErrors(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Exception e) throws ServletException, IOException {
        TokenException exception = new TokenException(e.getMessage());

        response.setStatus(exception.getResponseEntity().getStatusCode().value());
        response.setContentType("application/json");

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(exception.getResponseEntity().getBody()));

        filterChain.doFilter(request, response);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        JwtUserInfo jwtUserInfo = null;
        String jwtToken = null;

        if (authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
        } else {
            jwtToken = authHeader;
        }


        try {
            jwtUserInfo = jwtUtils.decodeToken(jwtToken);
        } catch (JWTVerificationException e) {
            throwErrors(request, response, filterChain, e);
            return;
        }

        if (jwtUserInfo.getEmail() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserPrincipal userPrincipal = (UserPrincipal) customUserDetailsService.loadUserByUsername(jwtUserInfo.getEmail());
                if (jwtUtils.isTokenValid(jwtToken, userPrincipal)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userPrincipal, jwtToken, userPrincipal.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
                filterChain.doFilter(request, response);
            } catch (UsernameNotFoundException e) {
                throwErrors(request, response, filterChain, e);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
