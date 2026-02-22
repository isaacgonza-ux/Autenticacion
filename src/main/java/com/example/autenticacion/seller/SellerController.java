package com.example.autenticacion.seller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.autenticacion.auth.dto.UserProfileResponse;
import com.example.autenticacion.user.User;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;




@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    
    @GetMapping("/dashboard")
    public ResponseEntity<String> getSellerDashboard(@AuthenticationPrincipal User user) {
        try {
            String response = sellerService.getDashboardInfo(user);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    
     
    @GetMapping("/account")
    public ResponseEntity<String> getSellerAccount(@AuthenticationPrincipal User user) {
        String response = sellerService.getSellerAccountDetails(user);
        return ResponseEntity.ok(response);
    }


    
    @GetMapping("/get_users")
    public ResponseEntity<Page<UserProfileResponse>> getAllUsers(
        @RequestParam(defaultValue = "0")int page,
        @RequestParam(defaultValue = "10")int size
    ) {
        
        return ResponseEntity.ok(sellerService.getAllUsers(page, size));
    }
}