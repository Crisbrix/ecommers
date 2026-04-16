package com.producto.producto.infrastructure.driver_adapters.jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.producto.producto.infrastructure.driver_adapters.jpa_repository.ProductoData;

import java.util.Optional;

public interface ProductoDataJpaRepository extends JpaRepository<ProductoData, Integer> {
    Optional<ProductoData> findByNombre(String nombre);
}
