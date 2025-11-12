package com.example.autenticacion.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    
    private static final String SECRET_KEY = "XWgEf7xRA6tkom6nODTX0W4GYYq6CnGOyzo+8QtJDnM=";

    public String getToken(UserDetails user) {

        System.out.println("=== GENERANDO TOKEN ===");
        System.out.println("Usuario: " + user.getUsername());
        System.out.println("Authorities: " + user.getAuthorities());
        String token =  buildToken(new HashMap<>(), user);

          
        System.out.println("Token generado: " + (token != null ? "SÍ" : "NULL"));
        System.out.println("Longitud token: " + (token != null ? token.length() : 0));
        
        return token;
    }

    private String buildToken(HashMap<String, Object> extraClaims, UserDetails user) {
       try{
        String token = Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

            System.out.println("Token construido exitosamente");
            return token;
       }catch(Exception e){
            System.err.println("ERROR al construir token: " + e.getMessage());
            e.printStackTrace();
            return null;
       }
    }

    private Key getKey() {
         try {
            byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
            System.out.println("Clave decodificada correctamente. Bytes: " + keyBytes.length);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            System.err.println("ERROR al decodificar clave: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName=getUsernameFromToken(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
        
    }

    private Claims getAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);

    }


    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }


    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}
