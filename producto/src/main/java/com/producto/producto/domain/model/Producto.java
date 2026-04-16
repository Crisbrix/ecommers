package com.producto.producto.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Producto {

    private Integer productoId;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;

    public void validar() {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }

        if (precio == null || precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }

        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }

}

