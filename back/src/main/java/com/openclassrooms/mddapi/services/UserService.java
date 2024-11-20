package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.UserProfileDTO;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for managing user-related operations such as registration, login, and
 * profile updates.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a UserService with the required dependencies.
     *
     * @param userRepository  the repository for user-related database operations.
     * @param passwordEncoder the encoder for hashing passwords.
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user.
     *
     * @param user the user to be registered.
     * @return the registered user.
     * @throws RuntimeException if the email is already in use.
     */
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user.
     * @return the user, or {@code null} if no user was found.
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * Logs in a user by validating their credentials.
     *
     * @param email    the email address of the user.
     * @param password the raw password of the user.
     * @return {@code true} if the login is successful, {@code false} otherwise.
     */
    public boolean loginUser(String email, String password) {
        User existingUser = userRepository.findByEmail(email).orElse(null);
        return existingUser != null && passwordEncoder.matches(password, existingUser.getPassword());
    }

    /**
     * Retrieves a user's profile by their ID.
     *
     * @param userId the ID of the user.
     * @return the user's profile.
     * @throws RuntimeException if the user is not found.
     */
    public User getUserProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Updates a user's profile with the provided information.
     *
     * @param userId         the ID of the user to be updated.
     * @param userUpdatesDTO the data transfer object containing the updated user
     *                       information.
     * @return the updated user.
     * @throws RuntimeException if the user is not found.
     */
    public User updateUserProfile(Long userId, UserProfileDTO userUpdatesDTO) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userUpdatesDTO.getEmail() != null && !userUpdatesDTO.getEmail().isEmpty()) {
            existingUser.setEmail(userUpdatesDTO.getEmail());
        }

        if (userUpdatesDTO.getUsername() != null && !userUpdatesDTO.getUsername().isEmpty()) {
            existingUser.setUsername(userUpdatesDTO.getUsername());
        }

        return userRepository.save(existingUser);
    }
}
