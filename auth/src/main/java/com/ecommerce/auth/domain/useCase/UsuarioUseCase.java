package com.ecommerce.auth.domain.useCase;

import com.ecommerce.auth.domain.model.gateway.EncrypterGateway;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateWay;
import com.ecommerce.auth.domain.model.Usuario;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

@RequiredArgsConstructor
public class UsuarioUseCase {
    private final UsuarioGateWay usuarioGateWay;
    private final EncrypterGateway encrypterGateway;

    public Usuario guardarUsuario(Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getPassword() == null) {
            throw new NullPointerException("El email o password no puede ser nulo - guardarUsuario");
        }
        String passEncrypt = encrypterGateway.encrypt(usuario.getPassword());
        usuario.setPassword(passEncrypt);

        Usuario usuarioGuardado = usuarioGateWay.guardarUser(usuario);

        return usuarioGuardado;
    }

    public Usuario buscarUsuarioPorCedula(String cedula) {
        try {
            usuarioGateWay.buscarUsuarioPorCedula(cedula);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Usuario usuarioVacio = new Usuario();
            return usuarioVacio;
        }
        return usuarioGateWay.buscarUsuarioPorCedula(cedula);
    }

    public void eliminarUsuarioPorCedula(String cedula) {
        try{
            usuarioGateWay.eliminarUsuarioPorCedula(cedula);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public Usuario actualizarUsuario(String cedula, Usuario usuario) {
        return usuarioGateWay.actualizarUsuario(cedula, usuario);
    }

    public Usuario login(String email, String password) {
        Usuario usuario = usuarioGateWay.buscarPorEmail(email);

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado o el email no tiene el @");
        }
        if (!email.contains("@")) {
            throw new RuntimeException("El email no tiene el @");
        }
        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        return usuario;
    }
}
