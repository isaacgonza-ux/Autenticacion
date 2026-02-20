package com.example.autenticacion.token;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import com.example.autenticacion.user.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pwd_reset_token_seq")
    @SequenceGenerator(name = "pwd_reset_token_seq", sequenceName = "PWD_RESET_TOKEN_SEQUENCE", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "used")
    private Boolean used = false;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (used == null) {
            used = false;
        }
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}