package com.producto.producto.domain.model.gateway;

import com.producto.producto.domain.model.Producto;

public interface ProductoGateWay{
    Producto guardarProducto(Producto producto);
    Producto buscarProductoPorId(Integer productoId);
    void eliminarProductoPorId(Integer productoId);
    Producto actualizarProducto(Integer productoId, Producto producto);
    Producto validarProductoPorNombre(String nombre);
}
