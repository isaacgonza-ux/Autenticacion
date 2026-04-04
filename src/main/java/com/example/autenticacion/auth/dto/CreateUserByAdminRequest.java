package com.example.autenticacion.auth.dto;

import com.example.autenticacion.user.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserByAdminRequest {

    @NotBlank(message="El nombre de usuario no puede estar vacío")
    private String username;

    @NotBlank(message="La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

     @NotBlank(message="El nombre no puede estar vacío")
    private String name;

    @NotBlank(message="El email no puede estar vacío")
    @Email(message="El formato del email no es válido")
    private String email;

    private Role role; 
}
