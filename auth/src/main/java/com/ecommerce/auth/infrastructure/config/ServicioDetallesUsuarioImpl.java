package com.ecommerce.auth.infrastructure.config;

import com.ecommerce.auth.domain.model.gateway.UsuarioGateWay;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicioDetallesUsuarioImpl implements UserDetailsService {

    private final UsuarioGateWay puertaEnlaceUsuario;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.ecommerce.auth.domain.model.Usuario usuario = puertaEnlaceUsuario.buscarPorEmail(email);
        
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
        }

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .roles(usuario.getRole() != null ? usuario.getRole() : "USER")
                .build();
    }
}
