package com.example.autenticacion.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.example.autenticacion.user.User;

import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    List<RefreshToken> findByUser(User user);
    void deleteByUser(User user);

    @Modifying // Indica que es una operación de modificación (delete/update)
    void deleteByExpiresAtBefore(LocalDateTime now);
}
