package com.ecommerce.auth.domain.model.gateway;

import com.ecommerce.auth.domain.model.Usuario;
public interface UsuarioGateWay{
    Usuario guardarUser(Usuario usuario);
    Usuario buscarUsuarioPorCedula(String cedula);
    void eliminarUsuarioPorCedula(String cedula);
    Usuario actualizarUsuario(String cedula, Usuario usuario);
    Usuario buscarPorEmail(String email);
}
