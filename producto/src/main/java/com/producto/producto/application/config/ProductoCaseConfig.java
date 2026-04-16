package com.producto.producto.application.config;

import com.producto.producto.domain.model.gateway.ProductoGateWay;
import com.producto.producto.domain.useCase.ProductoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductoCaseConfig {
    @Bean
    public ProductoUseCase productoUseCase(ProductoGateWay productoGateWay) {
        return new ProductoUseCase(productoGateWay);
    }
}
