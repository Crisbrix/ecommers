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

}

