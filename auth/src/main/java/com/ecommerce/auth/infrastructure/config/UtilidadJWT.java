package com.ecommerce.auth.infrastructure.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class UtilidadJWT {

    @Value("${jwt.secreto:miClaveSecreta}")
    private String secreto;

    @Value("${jwt.duracion:86400000}")
    private Long duracion;

    private SecretKey obtenerClaveFirma() {
        return Keys.hmacShaKeyFor(secreto.getBytes());
    }

    public String extraerNombreUsuario(String token) {
        return extraerReclamo(token, Claims::getSubject);
    }

    public Date extraerFechaExpiracion(String token) {
        return extraerReclamo(token, Claims::getExpiration);
    }

    public <T> T extraerReclamo(String token, Function<Claims, T> resolvedorReclamos) {
        final Claims reclamos = extraerTodosLosReclamos(token);
        return resolvedorReclamos.apply(reclamos);
    }

    private Claims extraerTodosLosReclamos(String token) {
        return Jwts.parser()
                .verifyWith(obtenerClaveFirma())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean tokenHaExpirado(String token) {
        return extraerFechaExpiracion(token).before(new Date());
    }

    public String generarToken(String nombreUsuario) {
        Map<String, Object> reclamos = new HashMap<>();
        return crearToken(reclamos, nombreUsuario);
    }

    public String generarToken(String nombreUsuario, String rol) {
        Map<String, Object> reclamos = new HashMap<>();
        reclamos.put("role", rol);
        return crearToken(reclamos, nombreUsuario);
    }

    private String crearToken(Map<String, Object> reclamos, String sujeto) {
        return Jwts.builder()
                .claims(reclamos)
                .subject(sujeto)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + duracion))
                .signWith(obtenerClaveFirma())
                .compact();
    }

    public Boolean validarToken(String token, String nombreUsuario) {
        final String nombreUsuarioExtraido = extraerNombreUsuario(token);
        return (nombreUsuarioExtraido.equals(nombreUsuario) && !tokenHaExpirado(token));
    }

    public Boolean validarToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(obtenerClaveFirma())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
