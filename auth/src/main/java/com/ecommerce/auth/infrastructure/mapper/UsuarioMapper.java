package com.ecommerce.auth.infrastructure.mapper;


import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.infrastructure.driver_adapters.jpa_repository.UsuarioData;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public Usuario toUsuario(UsuarioData usuarioData) {
        return new Usuario(
                usuarioData.getCedula(),
                usuarioData.getNombre(),
                usuarioData.getEmail(),
                usuarioData.getPassword(),
                usuarioData.getEdad(),
                usuarioData.getTelefono(),
                usuarioData.getRole()
        );
    }

    public UsuarioData toUsuarioData(Usuario usuario) {
        return new UsuarioData(
                usuario.getCedula(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getEdad(),
                usuario.getTelefono(),
                usuario.getRole()
        );
    }
}
