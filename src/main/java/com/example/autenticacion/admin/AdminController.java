package com.example.autenticacion.admin;


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

import com.example.autenticacion.auth.dto.CreateUserByAdminRequest;
import com.example.autenticacion.auth.dto.MessageResponse;
import com.example.autenticacion.auth.dto.UserProfileResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/create_users")
    public ResponseEntity<UserProfileResponse> createUser(@RequestBody CreateUserByAdminRequest request) {
        return ResponseEntity.ok(adminService.createUser(request));
    }

    @GetMapping("/get_users")
    public ResponseEntity<Page<UserProfileResponse>> getAllUsers(
        @RequestParam(defaultValue = "0")int page,
        @RequestParam(defaultValue = "10")int size
    ) {
        
        return ResponseEntity.ok(adminService.getAllUsers(page, size));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Integer id) {
        return ResponseEntity.ok(adminService.deleteUser(id));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserProfileResponse> updateUser(
            @PathVariable Integer id,
            @RequestBody CreateUserByAdminRequest request) {
        return ResponseEntity.ok(adminService.updateUser(id, request));
    }
}
