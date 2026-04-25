package com.producto.producto.infrastructure.entry_points;


import com.producto.producto.domain.model.Producto;
import com.producto.producto.domain.useCase.ProductoUseCase;
import com.producto.producto.infrastructure.mapper.ProductoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.producto.producto.infrastructure.driver_adapters.jpa_repository.ProductoData;

import java.util.Map;
// se agrego el cors para el uso del front
@CrossOrigin(origins = "*", methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE,
        RequestMethod.OPTIONS
})

//asignamos la URL
@RestController
@RequestMapping("/api/ecommerce/Productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoUseCase productoUseCase;
    private final ProductoMapper productoMapper;

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarProducto(@RequestBody ProductoData productoData) { //request sirve para pasar de json a objeto
        try {

            Producto producto = productoUseCase.guardarProducto(
                    productoMapper.toProducto(productoData)
            );

            return ResponseEntity.ok(producto);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.ok(
                    Map.of(
                            "success", false,
                            "mensaje1", e.getMessage()
                    )
            );
        }
    }

    @GetMapping("/Productos/{productoId}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Integer productoId) { //path captura los datos que pasamos por url
        Producto producto = productoUseCase.buscarProductoPorId(productoId);
        return new ResponseEntity<>(producto, HttpStatus.OK);
    }

    @DeleteMapping("/Productos/{productoId}")
    public ResponseEntity<String> eliminarProducto(@PathVariable Integer productoId) {
        productoUseCase.eliminarProductoPorId(productoId);
        return ResponseEntity.ok("Producto eliminado correctamente");
    }
    @PutMapping("/Productos/{productoId}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Integer productoId, @RequestBody ProductoData productoData) {
        Producto ProductoActualizado = productoUseCase.actualizarProducto(productoId,  productoMapper.toProducto(productoData));
        return ResponseEntity.ok(ProductoActualizado);
    }
}
