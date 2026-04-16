package com.ecommerce.auth.infrastructure.driver_adapters.jpa_repository;

import com.ecommerce.auth.domain.model.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="usuarios")
@Data
public class UsuarioData{
    @Id
    private String cedula;
    private String nombre;
    @Column(length = 30, nullable = false)
    private String email;
    private String password;
    private Integer edad;
    @Column(length = 10)
    private String telefono;
    private String role;
}
