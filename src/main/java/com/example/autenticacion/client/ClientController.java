package com.example.autenticacion.client;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    // Crear un nuevo cliente
    @PostMapping("client/create")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client created = clientService.createClient(client);
        return ResponseEntity.ok(created);
    }

    // Obtener un cliente por su ID
    @GetMapping("client/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable int id) {
        Client client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    // Listar todos los clientes
    @GetMapping("client/all")
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    // Actualizar un cliente existente
    @PutMapping("client/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable int id, @RequestBody Client client) {
        Client updated = clientService.updateClient(id, client);
        return ResponseEntity.ok(updated);
    }

    // Eliminar un cliente
    @DeleteMapping("client/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable int id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    // Desactivar un cliente
    @PutMapping("client/{id}/disable")
    public ResponseEntity<Client> disableClient(@PathVariable int id) {
        Client disabled = clientService.disableClient(id);
        return ResponseEntity.ok(disabled);
    }
}
