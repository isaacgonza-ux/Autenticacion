package com.example.autenticacion.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email no es válido")
    String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    String password;
    
}
