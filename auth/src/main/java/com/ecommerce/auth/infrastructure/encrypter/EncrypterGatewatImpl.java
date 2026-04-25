package com.ecommerce.auth.infrastructure.encrypter;

import com.ecommerce.auth.domain.model.gateway.EncrypterGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class EncrypterGatewatImpl implements EncrypterGateway {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encrypt(String password) {
        return encoder.encode(password);
    }
}
