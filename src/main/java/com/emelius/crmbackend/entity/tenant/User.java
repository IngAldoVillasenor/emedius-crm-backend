package com.emelius.crmbackend.entity.tenant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "users") // Usamos "users" porque "user" es una palabra reservada en SQL
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // Aquí guardaremos "ROLE_ADMIN" o "ROLE_STAFF"
}