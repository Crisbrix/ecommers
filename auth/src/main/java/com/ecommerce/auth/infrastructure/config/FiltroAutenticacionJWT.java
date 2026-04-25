package com.ecommerce.auth.infrastructure.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FiltroAutenticacionJWT extends OncePerRequestFilter {

    private final UtilidadJWT utilidadJWT;
    private final ServicioDetallesUsuarioImpl servicioDetallesUsuario;

    @Override
    protected void doFilterInternal(HttpServletRequest peticion, HttpServletResponse respuesta, FilterChain cadenaFiltros)
            throws ServletException, IOException {
        
        final String cabeceraAutorizacion = peticion.getHeader("Authorization");

        String nombreUsuario = null;
        String jwt = null;

        if (cabeceraAutorizacion != null && cabeceraAutorizacion.startsWith("Bearer ")) {
            jwt = cabeceraAutorizacion.substring(7);
            try {
                nombreUsuario = utilidadJWT.extraerNombreUsuario(jwt);
            } catch (Exception e) {
                logger.error("La extracción del token JWT falló: " + e.getMessage());
            }
        }

        if (nombreUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails detallesUsuario = this.servicioDetallesUsuario.loadUserByUsername(nombreUsuario);

            if (utilidadJWT.validarToken(jwt, detallesUsuario.getUsername())) {
                UsernamePasswordAuthenticationToken autenticacion = new UsernamePasswordAuthenticationToken(
                        detallesUsuario, null, detallesUsuario.getAuthorities());
                autenticacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(peticion));
                SecurityContextHolder.getContext().setAuthentication(autenticacion);
            }
        }
        
        cadenaFiltros.doFilter(peticion, respuesta);
    }
}
