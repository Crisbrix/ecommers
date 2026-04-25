package com.ecommerce.auth.infrastructure.entry_points;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.model.LoginRequest;
import com.ecommerce.auth.domain.useCase.UsuarioUseCase;
import com.ecommerce.auth.infrastructure.driver_adapters.jpa_repository.UsuarioData;
import com.ecommerce.auth.infrastructure.mapper.UsuarioMapper;
import com.ecommerce.auth.infrastructure.config.UtilidadJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ecommerce/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;
    private final UsuarioMapper usuarioMapper;
    private final UtilidadJWT utilidadJWT;

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    @PostMapping("/guardar")
    public ResponseEntity <Usuario> guardarUsuario(@RequestBody UsuarioData usuarioData) {
        Usuario usuarioValidadoGuardado = usuarioUseCase.guardarUsuario(usuarioMapper.toUsuario(usuarioData));
        return new ResponseEntity<>(usuarioValidadoGuardado, HttpStatus.OK);
    }
    @GetMapping("/usuarios/{cedula}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Usuario> obtenerUsuarioPorCedula(@PathVariable String cedula) {
        Usuario usuario = usuarioUseCase.buscarUsuarioPorCedula(cedula);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @DeleteMapping("/usuarios/{cedula}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminarUsuario(@PathVariable String cedula) {
        usuarioUseCase.eliminarUsuarioPorCedula(cedula);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }
    @PutMapping("/usuarios/{cedula}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable String cedula, @RequestBody UsuarioData usuarioData) {
        Usuario usuarioActualizado = usuarioUseCase.actualizarUsuario(cedula,  usuarioMapper.toUsuario(usuarioData));
        return ResponseEntity.ok(usuarioActualizado);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        try {
            Usuario usuario = usuarioUseCase.login(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );

            String token = utilidadJWT.generarToken(usuario.getEmail(), usuario.getRole());

            return ResponseEntity.ok(
                    Map.of(
                            "mensaje", "Login exitoso",
                            "token", token,
                            "usuario", Map.of(
                                    "email", usuario.getEmail(),
                                    "nombre", usuario.getNombre(),
                                    "role", usuario.getRole()
                            )
                    )
            );

        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            Map.of(
                                    "error", true,
                                    "mensaje", e.getMessage()
                            )
                    );
        }
    }
}
