package com.openclassrooms.mddapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.MalformedJwtException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    @Value("${mddapi.jwtSecret}")
    private String jwtSecret;

    @Value("${mddapi.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Le token JWT est expiré : " + e.getMessage());
        } catch (SignatureException e) {
            System.out.println("Signature JWT invalide : " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Format JWT invalide : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erreur lors de la validation du JWT : " + e.getMessage());
        }
        return false;
    }
}
