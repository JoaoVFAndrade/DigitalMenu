package com.br.digitalmenu.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Collection;
import java.util.Date;

public class JwtUtil {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 86400000; // configurei para um dia para podermos testar

    public static String generateToken(String username, Collection<?> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles) //aqui passamos a role conforme o user como garcom ou adm
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public static String validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    // Retorna todos os claims (user, role, exp, etc.)
    public static Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Facilita pegar só o user
    public static String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Facilita pegar só a role
    public static String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    @SuppressWarnings("unchecked")
    public static Collection<String> getRoles(String token) {
        return getClaims(token).get("roles", Collection.class);
    }
}
