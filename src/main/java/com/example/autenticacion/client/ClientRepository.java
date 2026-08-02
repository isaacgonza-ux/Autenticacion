package com.example.autenticacion.client;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    boolean existsByEmail(String email);
}
