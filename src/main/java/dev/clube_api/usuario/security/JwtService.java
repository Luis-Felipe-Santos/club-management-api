package dev.clube_api.usuario.security;

import dev.clube_api.usuario.model.UsuarioModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiracao}")
    private long expiracao;

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String gerarToken(UsuarioModel usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("role", usuario.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiracao))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extrairEmail(String token) {
        return getClaims(token).getSubject();
    }

    public String extrairRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public boolean tokenValido(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
