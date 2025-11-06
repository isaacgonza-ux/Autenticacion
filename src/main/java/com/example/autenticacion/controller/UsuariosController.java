package com.example.autenticacion.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/usuarios")
public class UsuariosController {

    public String welcome(){
        return "Welcome to Usuarios API";
        }
    
}
