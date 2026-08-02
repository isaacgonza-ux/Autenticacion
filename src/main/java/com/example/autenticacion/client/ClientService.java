package com.example.autenticacion.client;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Client createClient(Client client) {
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());
        client.setStatus(true);
        return clientRepository.save(client);
    }

    public Client getClientById(int id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client updateClient(int id, Client updatedClient) {
        Client client = getClientById(id);
        
        if (updatedClient.getName() != null) {
            client.setName(updatedClient.getName());
        }
        if (updatedClient.getLastName() != null) {
            client.setLastName(updatedClient.getLastName());
        }
        if (updatedClient.getEmail() != null) {
            if (!updatedClient.getEmail().equals(client.getEmail()) 
                    && clientRepository.existsByEmail(updatedClient.getEmail())) {
                throw new RuntimeException("El email ya está registrado");
            }
            client.setEmail(updatedClient.getEmail());
        }
        if (updatedClient.getPhone() != null) {
            client.setPhone(updatedClient.getPhone());
        }
        
        client.setUpdatedAt(LocalDateTime.now());
        return clientRepository.save(client);
    }

    public void deleteClient(int id) {
        Client client = getClientById(id);
        clientRepository.delete(client);
    }

    public Client disableClient(int id) {
        Client client = getClientById(id);
        client.setStatus(false);
        client.setUpdatedAt(LocalDateTime.now());
        return clientRepository.save(client);
    }
}
