package com.emelius.crmbackend.config;

import com.emelius.crmbackend.entity.tenant.User;
import com.emelius.crmbackend.repository.tenant.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner seedAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Buscamos si ya existe el correo del admin para no duplicarlo
            if (userRepository.findByEmail("admin@emediusgw.com").isEmpty()) {

                User admin = new User();
                //admin.setName("Emedius Admin");
                admin.setEmail("admin@emediusgw.com");

                // Si usas Spring Security, el password DEBE ir encriptado:
                admin.setPassword(passwordEncoder.encode("TallerEmedius2026"));

                // Si NO usas Spring Security y guardas en texto plano (solo para MVP), usa esto:
                // admin.setPassword("TallerEmedius2026");

                admin.setRole("ADMIN"); // Si tienes roles en tu sistema, descomenta esto

                userRepository.save(admin);
                System.out.println("✅ SEMBRADOR: Usuario Admin creado exitosamente en la Base de Datos.");
            } else {
                System.out.println("⚡ SEMBRADOR: El usuario Admin ya existe. Omitiendo creación.");
            }
        };
    }
}