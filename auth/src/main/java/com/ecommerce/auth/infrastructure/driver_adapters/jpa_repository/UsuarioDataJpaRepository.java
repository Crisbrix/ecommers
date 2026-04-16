package com.ecommerce.auth.infrastructure.driver_adapters.jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioDataJpaRepository extends JpaRepository<UsuarioData, String> {
    UsuarioData findByEmail(String email);
}
