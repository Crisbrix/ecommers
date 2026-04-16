package com.producto.producto.domain.useCase;

import com.producto.producto.domain.model.Producto;
import com.producto.producto.domain.model.gateway.ProductoGateWay;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductoUseCase {
    private final ProductoGateWay productoGateWay;
    public Producto guardarProducto(Producto producto) {

        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (producto.getNombre().trim().length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres");
        }
        if (producto.getPrecio() == null) {
            throw new IllegalArgumentException("El precio es obligatorio");
        }
        if (producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if(producto.getStock() == null) {
            throw new IllegalArgumentException("El stock es obligatorio");
        }
        if(producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no debe ser negativo");
        }
        if (producto.getDescripcion() != null && producto.getDescripcion().length() > 255) {
            throw new IllegalArgumentException("La descripción es demasiado larga");
        }
        if (productoGateWay.validarProductoPorNombre(producto.getNombre()) != null) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre");
        }

        return productoGateWay.guardarProducto(producto);
    }

    public Producto buscarProductoPorId(Integer productoId) {
        try {
            productoGateWay.buscarProductoPorId(productoId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Producto usuarioVacio = new Producto();
            return usuarioVacio;
        }
        return productoGateWay.buscarProductoPorId(productoId);
    }

    public void eliminarProductoPorId(Integer productoId) {
        try{
            productoGateWay.eliminarProductoPorId(productoId);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public Producto actualizarProducto(Integer productoId, Producto producto) {
        return productoGateWay.actualizarProducto(productoId, producto);
    }

    public Producto validarProductoPorNombre(String nombre) {
        Producto producto = productoGateWay.validarProductoPorNombre(nombre);
        if (productoGateWay.validarProductoPorNombre(producto.getNombre()) != null) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre");
        }
        return producto;
    }
}
