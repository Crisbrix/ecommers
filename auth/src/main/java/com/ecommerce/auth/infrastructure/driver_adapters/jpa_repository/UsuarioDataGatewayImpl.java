package com.ecommerce.auth.infrastructure.driver_adapters.jpa_repository;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateWay;
import com.ecommerce.auth.infrastructure.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor


@Repository
public class UsuarioDataGatewayImpl implements UsuarioGateWay {

    private final UsuarioDataJpaRepository repository;
    private final UsuarioMapper mapper;
    @Override
    public Usuario guardarUser(Usuario usuario) {
        UsuarioData usuariodata = mapper.toUsuarioData(usuario);
        return mapper.toUsuario(repository.save(usuariodata));
    }
    @Override
    public Usuario buscarUsuarioPorCedula(String cedula) {
        return repository.findById(cedula)
                .map(mapper::toUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    @Override
    public void eliminarUsuarioPorCedula(String cedula) {
        if (!repository.existsById(cedula)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        repository.deleteById(cedula);
    }
    @Override
    public Usuario actualizarUsuario(String cedula, Usuario usuario) {
        UsuarioData existente = repository.findById(cedula) .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        existente.setNombre(usuario.getNombre());
        existente.setEmail(usuario.getEmail());
        existente.setPassword(usuario.getPassword());
        existente.setEdad(usuario.getEdad());
        existente.setTelefono(usuario.getTelefono());
        existente.setRole(usuario.getRole());

        UsuarioData actualizado = repository.save(existente);

        return mapper.toUsuario(actualizado);
    }
    @Override
    public Usuario buscarPorEmail(String email) {
        UsuarioData usuarioData = repository.findByEmail(email);

        if (usuarioData == null) {
            return null;
        }
        return mapper.toUsuario(usuarioData);
    }
}
