package com.example.autenticacion.client;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients", uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "email"})})
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_seq")
    @SequenceGenerator(name = "client_seq", sequenceName = "client_SEQUENCE", allocationSize = 1)
    private int id;

    @Column(nullable = false)
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String nameCompany;
    private String nameProject;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean status;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Company> companies;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Project> projects;
}
