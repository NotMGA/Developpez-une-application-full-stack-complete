package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.UserProfileDTO;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        // Vérifiez si l'email est déjà utilisé
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        // Encodez le mot de passe avant de l'enregistrer
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public boolean loginUser(String email, String password) {
        User existingUser = userRepository.findByEmail(email).orElse(null);
        if (existingUser != null && passwordEncoder.matches(password, existingUser.getPassword())) {
            return true; // Login successful
        } else {
            return false; // Login failed
        }
    }

    @Override
    public User getUserProfile(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User updateUserProfile(Long userId, UserProfileDTO userUpdatesDTO) {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (userUpdatesDTO.getEmail() != null && !userUpdatesDTO.getEmail().isEmpty()) {
            existingUser.setEmail(userUpdatesDTO.getEmail());
        }

        if (userUpdatesDTO.getUsername() != null && !userUpdatesDTO.getUsername().isEmpty()) {
            existingUser.setUsername(userUpdatesDTO.getUsername());
        }

        return userRepository.save(existingUser);
    }

}
