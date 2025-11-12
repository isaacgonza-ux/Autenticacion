package com.example.autenticacion.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
public class AdminProductProxyController {

    private final RestTemplate restTemplate;

    
    @GetMapping
    public Object getProducts(){
        String url="https://fakestoreapi.com/products";
        return restTemplate.getForObject(url, Object.class);
    }
    
}
