package com.fabio.estacionamento.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUserDetailsService detailsService;

    //Método que interceptas as requisições e verifica se o token é válido
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = request.getHeader(JwtUtils.JWT_AUTHORIZATION);
        if (token == null || !token.startsWith(JwtUtils.JWT_BEARER)) { //Verifica se o token é nulo, vazio ou não começa com Bearer
            log.info("JWT está nulo, vazio ou não começa com Bearer.");
            filterChain.doFilter(request, response); //Se for, passa para o próximo filtro
            return;
        }
        if (JwtUtils.isTokenValid(token)){
            log.warn("JWT Token está inválido ou expirado.");
            filterChain.doFilter(request, response);
        }

        String username = JwtUtils.getUsernameFromToken(token); //Pega o usuário do token

        toAuthentication (request, username); //Autentica o usuário

        filterChain.doFilter(request, response);
    }

    //Método que autentica o usuário
    private void toAuthentication(HttpServletRequest request, String username) {
        UserDetails userDetails = detailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .authenticated(userDetails, null, userDetails.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
