package com.producto.producto.infrastructure.driver_adapters.jpa_repository;

import com.producto.producto.domain.model.Producto;
import com.producto.producto.domain.model.gateway.ProductoGateWay;
import com.producto.producto.infrastructure.mapper.ProductoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository

public class ProductoDataGatewayImpl implements ProductoGateWay {

    private final ProductoDataJpaRepository repository;
    private final ProductoMapper mapper;
    @Override
    public Producto guardarProducto(Producto producto) {
        ProductoData productodata = mapper.toProductoData(producto);
        return mapper.toProducto(repository.save(productodata));
    }
    @Override
    public Producto buscarProductoPorId(Integer productoId) {
        return repository.findById(productoId)
                .map(mapper::toProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }
    @Override
    public void eliminarProductoPorId(Integer productoId) {
        if (!repository.existsById(productoId)) {
            throw new RuntimeException("Producto no encontrado");
        }
        repository.deleteById(productoId);
    }
    @Override
    public Producto actualizarProducto(Integer productoId, Producto producto) {
        ProductoData existente = repository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        existente.setNombre(producto.getNombre());
        existente.setPrecio(producto.getPrecio());
        existente.setDescripcion(producto.getDescripcion()); // ✔️ corregido
        existente.setStock(producto.getStock());

        ProductoData actualizado = repository.save(existente);

        return mapper.toProducto(actualizado);
    }
    @Override
    public Producto validarProductoPorNombre(String nombre) {
        return repository.findByNombre(nombre)
                .map(mapper::toProducto)
                .orElse(null);
    }
}