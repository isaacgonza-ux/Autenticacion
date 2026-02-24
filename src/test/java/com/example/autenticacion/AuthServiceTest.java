package com.example.autenticacion;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.example.autenticacion.auth.AuthService;
import com.example.autenticacion.auth.dto.AuthResponse;
import com.example.autenticacion.auth.dto.LoginRequest;
import com.example.autenticacion.auth.dto.RegisterRequest;
import com.example.autenticacion.exception.GlobalExceptionHandler;
import com.example.autenticacion.jwt.JwtService;
import com.example.autenticacion.user.Role;
import com.example.autenticacion.user.User;
import com.example.autenticacion.user.UserRepository;

import ch.qos.logback.core.net.SocketConnector.ExceptionHandler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Optional;


import com.example.autenticacion.token.RefreshTokenRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RefreshTokenRepository refreshTokenRepository; 

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService; 

    //Cuando las credenciales son correctas devuelve un token
    @Test
    public void testLogin(){

        LoginRequest loginRequest = new LoginRequest("admin@tienda.com","admin123456");
        User mockUser = new User();
        mockUser.setEmail("admin@tienda.com");
        mockUser.setRole(Role.ADMIN);

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(mockUser));
        when(jwtService.getToken(any(User.class))).thenReturn("token.falso.123");
        when(jwtService.getRefreshToken(any(User.class))).thenReturn("refresh.token.falso.456");
        when(jwtService.getExpirationTime()).thenReturn(3600L); 
        when(authenticationManager.authenticate(any()))
        .thenReturn(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("token.falso.123", response.getToken());

        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
        verify(refreshTokenRepository, times(1)).save(any());


    }

    @Test
    public void testLoginFail(){
        LoginRequest loginRequest = new LoginRequest("fantasma@tienda.com", "1234");
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
        authService.login(loginRequest);
        });
        verify(refreshTokenRepository, never()).save(any());
    }

    @Test
    public void testRegister(){

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("admin123");
        registerRequest.setPassword("admin123456");
        registerRequest.setName("admin");
        registerRequest.setEmail("admin@tienda.com");

        User mockUser = new User();
        mockUser.setId(1); 
        mockUser.setUsername("admin123");
        mockUser.setPassword("contraseña_encriptada_$$$"); 
        mockUser.setEmail("admin@tienda.com");
        mockUser.setRole(Role.USER);

        when(passwordEncoder.encode(anyString())).thenReturn("contraseña_encriptada_$$$");

        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(jwtService.getToken(any(User.class))).thenReturn("nuevo.token.123");
        when(jwtService.getRefreshToken(any(User.class))).thenReturn("refresh.token.falso.456");


        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("nuevo.token.123", response.getToken());

       verify(userRepository, times(1)).save(any(User.class));
       verify(passwordEncoder, times(1)).encode(registerRequest.getPassword());

    }
        
    
}
