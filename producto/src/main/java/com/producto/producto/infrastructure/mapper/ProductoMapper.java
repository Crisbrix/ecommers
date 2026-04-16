package com.producto.producto.infrastructure.mapper;


import com.producto.producto.domain.model.Producto;
import com.producto.producto.infrastructure.driver_adapters.jpa_repository.ProductoData;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto toProducto(ProductoData productoData) {

        Double precio = convertirADouble(productoData.getPrecio(), "Precio");
        Double stock = convertirADouble(productoData.getStock(), "Stock");

        return new Producto(
                productoData.getProductoId(),
                productoData.getNombre(),
                productoData.getDescripcion(),
                productoData.getPrecio(),
                productoData.getStock()
        );
    }

    public ProductoData toProductoData(Producto producto) {
        return new ProductoData(
                producto.getProductoId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock()
        );
    }

    private Double convertirADouble(Object valor, String campo) {

        if (valor == null) return null;

        try {

            // Si ya es número
            if (valor instanceof Integer) {
                return ((Integer) valor).doubleValue();
            }

            if (valor instanceof Double) {
                return (Double) valor;
            }

            // Si es String
            if (valor instanceof String) {
                String str = ((String) valor).trim();

                if (!str.matches("^[0-9]+(\\.[0-9]+)?$")) {
                    throw new IllegalArgumentException(campo + " incorrecto");
                }

                return Double.parseDouble(str);
            }

        } catch (Exception e) {
            throw new IllegalArgumentException(campo + " incorrecto");
        }

        throw new IllegalArgumentException(campo + " incorrecto");
    }

}