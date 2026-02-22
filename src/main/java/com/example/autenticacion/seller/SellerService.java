package com.example.autenticacion.seller;

import com.example.autenticacion.user.User;
import com.example.autenticacion.auth.dto.UserProfileResponse;
import com.example.autenticacion.user.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final UserRepository userRepository;

    

    public String getDashboardInfo(User seller){
        if(!seller.isEnabled()){
            throw new IllegalArgumentException("Tu cuenta de vendedor esta desactivada");
        }else{
            return "Bienvenido al panel de vendedor"+seller.getName();
        }
    }


   public String getSellerAccountDetails(User seller){
    return String.format("Detalles de la Cuenta Comercial:\n" +
            "- Nombre: %s\n" +
            "- Correo de contacto: %s\n" +
            "- Vendedor verificado: %s\n" +
            "- Miembro desde: %s",
            seller.getName(),
            seller.getEmail(),
            seller.getEmailVerified() ? "Sí" : "Pendiente de verificación",
            seller.getCreatedAt());
   }

   //Listar usuarios
   public Page<UserProfileResponse>getAllUsers(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = userRepository.findAll(pageable);

        return usersPage.map(user-> UserProfileResponse.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .name(user.getName())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .emailVerified(user.getEmailVerified())
                    .createdAt(user.getCreatedAt())
                    .build());
   }

}
