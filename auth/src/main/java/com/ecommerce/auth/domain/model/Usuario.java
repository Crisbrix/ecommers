package com.ecommerce.auth.domain.model;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    private String cedula;
    private String nombre;
    private String email;
    private String password;
    private Integer edad;
    private String telefono;
    private String role;
}
