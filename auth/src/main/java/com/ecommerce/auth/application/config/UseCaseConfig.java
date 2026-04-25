package com.ecommerce.auth.application.config;

import com.ecommerce.auth.domain.model.gateway.EncrypterGateway;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateWay;
import com.ecommerce.auth.domain.useCase.UsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
    @Bean
    public UsuarioUseCase usuarioUseCase(UsuarioGateWay usuarioGateWay, EncrypterGateway encrypterGateway) {
        return new UsuarioUseCase(usuarioGateWay, encrypterGateway);
    }
}
