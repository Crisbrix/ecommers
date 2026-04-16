package com.ecommerce.auth.domain.useCase;

import com.ecommerce.auth.domain.model.gateway.UsuarioGateWay;
import com.ecommerce.auth.domain.model.Usuario;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

@RequiredArgsConstructor
public class UsuarioUseCase {
    private final UsuarioGateWay usuarioGateWay;

    public Usuario guardarUsuario(Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getPassword() == null) {
            throw new NullPointerException("El email o password no puede ser nulo - guardarUsuario");
        }
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

        //probar si funciona el validador
        if (email == null || !email.contains("@")) {
            throw new RuntimeException("El email no tiene el @");
        }

        Usuario usuario = usuarioGateWay.buscarPorEmail(email);

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return usuario;
    }
}
