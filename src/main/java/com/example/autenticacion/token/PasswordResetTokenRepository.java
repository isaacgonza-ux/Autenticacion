package com.example.autenticacion.token;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.autenticacion.user.User;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(User user);
}