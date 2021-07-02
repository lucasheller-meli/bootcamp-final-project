package com.mercadolibre.bootcamp_g1_final_project.security;

import com.mercadolibre.bootcamp_g1_final_project.entities.users.User;
import com.mercadolibre.bootcamp_g1_final_project.services.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthWithToken extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserService userService;

    public AuthWithToken(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = getToken(request);
        boolean isValid = tokenService.isTokenValid(token);
        if(isValid){
        authClient(token);
        }
        filterChain.doFilter(request,response);
    }
    private void authClient(String token){
        final Integer userId = tokenService.getUserIdByToken(token);
        final User user = userService.findById(userId);
        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private String getToken(HttpServletRequest request){
        final String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")){
            return null;
        }
        return token.substring(7,token.length());

    }
}
