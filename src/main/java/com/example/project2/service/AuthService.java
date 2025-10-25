package com.example.project2.service;

import com.example.project2.dto.RegistrationRequest;
import com.example.project2.model.Credentials;
import com.example.project2.model.User;
import com.example.project2.model.UserRole;
import com.example.project2.repository.CredentialsRepository;
import com.example.project2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean registerUser(RegistrationRequest request) {
        // Проверка уникальности email и username
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email уже используется");
        }

        if (credentialsRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Имя пользователя уже используется");
        }

        // Создание пользователя
        User user = new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone(),
                UserRole.CUSTOMER // По умолчанию роль CUSTOMER
        );

        User savedUser = userRepository.save(user);

        // Создание учетных данных
        Credentials credentials = new Credentials(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                savedUser
        );

        credentialsRepository.save(credentials);
        return true;
    }

    public void updateLastLogin(String username) {
        credentialsRepository.findByUsername(username).ifPresent(credentials -> {
            credentials.setLastLogin(LocalDateTime.now());
            credentials.setFailedLoginAttempts(0); // Сброс счетчика неудачных попыток
            credentialsRepository.save(credentials);
        });
    }
}