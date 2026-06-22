package com.emelius.crmbackend.service;

import com.emelius.crmbackend.dto.request.LoginRequestDTO;
import com.emelius.crmbackend.dto.request.RegisterRequestDTO;
import com.emelius.crmbackend.entity.tenant.User;
import com.emelius.crmbackend.repository.tenant.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Inyectamos el repositorio y el encriptador que configuramos en SecurityConfig
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String registerUser(RegisterRequestDTO request) {
        // 1. Verificamos que el email no exista ya
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado en este taller.");
        }

        // 2. Creamos la entidad
        User user = new User();
        user.setEmail(request.getEmail());

        // 3. ¡LA MAGIA! Encriptamos la contraseña antes de guardarla
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 4. Le asignamos rol de Administrador por defecto
        user.setRole("ROLE_ADMIN");

        // 5. Guardamos en la BD
        userRepository.save(user);

        return "Usuario administrador creado exitosamente.";
    }

    public String login(LoginRequestDTO request) {
        // 1. Buscamos al usuario
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        // 2. Verificamos la contraseña usando BCrypt
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        // 3. Si todo es correcto, generamos el token
        return jwtService.generateToken(user.getEmail());
    }
}