package com.example.autenticacion;

import com.example.autenticacion.user.User;
import com.example.autenticacion.user.UserRepository;
import com.example.autenticacion.user.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        if (userRepository.count() > 0) {
            System.out.println("Ya existen usuarios. Omitiendo carga inicial.");
            return;
        }
        
        System.out.println("Iniciando prueba de estrés de base de datos...");
        long startTime = System.currentTimeMillis();
        
        Faker faker = new Faker();

        // 1. Crear Admin 
        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123456"))
                .name("Administrator")
                .email("admin@tienda.com")
                .role(Role.ADMIN)
                .emailVerified(true)
                .build();
        userRepository.save(admin);

        // 2. OPTIMIZACIÓN CLAVE: Encriptar la contraseña genérica UNA SOLA VEZ
        String defaultPasswordHash = passwordEncoder.encode("password123");
        
        // 3. OPTIMIZACIÓN CLAVE: Preparar la lista para los lotes
        List<User> batchList = new ArrayList<>();
        int totalUsersToCreate = 100; // <--- Cambia esto para probar tus límites (Ej: 1000, 10000, 50000)
        int batchSize = 20; // Enviamos a Oracle lo que decidas aca

        for (int i = 1; i <= totalUsersToCreate; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String username = firstName.toLowerCase() + i;
            
            User user = User.builder()
                    .username(username)
                    .password(defaultPasswordHash) // Usamos el hash pre-calculado
                    .name(firstName + " " + lastName)
                    .email(username + "@email.com")
                    .role(Role.USER)
                    .emailVerified(faker.bool().bool())
                    .build();
            
            batchList.add(user);

            // Cuando acumulamos 1000, disparamos a la base de datos
            if (i % batchSize == 0) {
                userRepository.saveAll(batchList); // Un solo viaje a la red por cada 1000
                System.out.println("Lote insertado: " + i + "/" + totalUsersToCreate);
                batchList.clear(); // Limpiamos la RAM de tu laptop
            }
        }

        // Guardar cualquier sobrante si el total no es múltiplo de 1000
        if (!batchList.isEmpty()) {
            userRepository.saveAll(batchList);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Usuario admin credenciales: admin/admin123456");
        System.out.println("===========================================");
        System.out.println("🚀 PRUEBA DE RENDIMIENTO COMPLETADA");
        System.out.println("📊 Se insertaron " + totalUsersToCreate + " usuarios.");
        System.out.println("⏱️ Tiempo total: " + (endTime - startTime) + " ms (" + ((endTime - startTime)/1000) + " segundos)");
        System.out.println("===========================================");
    }
}