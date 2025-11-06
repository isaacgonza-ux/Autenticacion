package com.example.autenticacion.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.autenticacion.user.UserRepository;
import com.example.autenticacion.user.User;
import com.example.autenticacion.jwt.JwtService;
import com.example.autenticacion.user.Role;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        
        return null;
    }

    public AuthResponse register(RegisterRequest request) {
        
        User user = User.builder()
            .username(request.getUsername())
            .password(request.getPassword())
            .name(request.getName())
            .email(request.getEmail())
            .role(Role.USER)
            .build();

        userRepository.save(user);

        return AuthResponse.builder()
            .token(jwtService.getToken(user))
            .build();
    }

}
