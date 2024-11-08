package com.duongw.universalpetcare.security.jwt;

import com.duongw.universalpetcare.security.user.UPCUserDetail;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;


import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.List;
import java.util.Date;
@Getter
@Component
public class JwtUtils {

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;
    @Value("${auth.token.expirationInMils}")
    private int jwtExpirationMs;



    public String generateTokenForUser(Authentication authentication) {
        UPCUserDetail upcUserPrincipal = (UPCUserDetail) authentication.getPrincipal();
        List<String> roles = upcUserPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .setSubject(upcUserPrincipal.getUsername())
                .claim("id", upcUserPrincipal.getId())
                .claim("roles", roles)
                .setIssuedAt(new java.util.Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), io.jsonwebtoken.SignatureAlgorithm.HS256)
                .compact();


    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }


    public String getUserNameFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            return true;
        }catch(MalformedJwtException | IllegalArgumentException | UnsupportedJwtException | ExpiredJwtException e){
            throw new JwtException(e.getMessage());
        }

    }

}
