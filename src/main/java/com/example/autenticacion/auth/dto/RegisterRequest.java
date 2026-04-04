package com.example.autenticacion.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message="El nombre de usuario no puede estar vacío")
    String username;

    @NotBlank(message="La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    String password;
    
    @NotBlank(message="El nombre no puede estar vacío")
    String name;

    @NotBlank(message="El email no puede estar vacío")
    @Email(message="El formato del email no es válido")
    String email;

    
}