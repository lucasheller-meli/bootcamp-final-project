package com.mercadolibre.bootcamp_g1_final_project.security;

import com.mercadolibre.bootcamp_g1_final_project.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    private String expiration;

    private String secret = "asjeb@$*DNS@(CUY@C(#ENDCBF#R*R";

    public String generateToken(Authentication authentication){
        final User user = (User) authentication.getPrincipal();
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1440000);
        return Jwts.builder()
                .setIssuer("Login API")
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    public boolean isTokenValid(String token){
        try{
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Integer getUserIdByToken(String token){
        final Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return Integer.parseInt(claims.getSubject());
    }


}
