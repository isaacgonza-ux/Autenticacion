package com.example.autenticacion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/dashboard")
    public ResponseEntity<String>dashboard(){
        return ResponseEntity.ok("Bienvenido al panel de admin");
    }


    
    
}
