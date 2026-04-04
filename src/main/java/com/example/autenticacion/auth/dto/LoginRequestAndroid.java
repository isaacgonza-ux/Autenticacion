package com.example.autenticacion.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestAndroid {
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    String username;
    @NotBlank(message = "La contraseña no puede estar vacía")
    String password;
    
}
