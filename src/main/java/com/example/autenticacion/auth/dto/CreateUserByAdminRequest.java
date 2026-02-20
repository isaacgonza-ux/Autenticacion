package com.example.autenticacion.auth.dto;

import com.example.autenticacion.user.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserByAdminRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    private Role role; // <- IMPORTANTE: aquí va el rol
}
