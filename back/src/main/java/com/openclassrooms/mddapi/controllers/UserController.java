package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.UserLoginDTO;
import com.openclassrooms.mddapi.dto.UserProfileDTO;
import com.openclassrooms.mddapi.dto.UserRegistrationDTO;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.security.JwtUtils;
import com.openclassrooms.mddapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

/**
 * REST controller for managing user-related operations, including registration,
 * login,
 * profile retrieval, and profile updates.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Registers a new user.
     *
     * @param userRegistrationDTO the data transfer object containing registration
     *                            details (email, username, password).
     * @return a {@link ResponseEntity} containing a success message or an error
     *         message in case of failure.
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        try {
            User user = new User();
            user.setEmail(userRegistrationDTO.getEmail());
            user.setUsername(userRegistrationDTO.getUsername());
            user.setPassword(userRegistrationDTO.getPassword());
            userService.registerUser(user);
            return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     * Authenticates a user and generates a JWT token upon successful login.
     *
     * @param userLoginDTO the data transfer object containing login credentials
     *                     (email and password).
     * @return a {@link ResponseEntity} containing a JWT token if authentication is
     *         successful,
     *         or an error message if authentication fails.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody UserLoginDTO userLoginDTO) {
        boolean isAuthenticated = userService.loginUser(userLoginDTO.getEmail(), userLoginDTO.getPassword());
        if (isAuthenticated) {
            String token = jwtUtils.generateJwtToken(userLoginDTO.getEmail());
            return ResponseEntity.ok(Collections.singletonMap("token", "Bearer " + token));
        } else {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid credentials"));
        }
    }

    /**
     * Retrieves the profile of the currently authenticated user.
     *
     * @return a {@link ResponseEntity} containing the user's profile details as a
     *         {@link UserProfileDTO},
     *         or an error status if the user is not authenticated or not found.
     */
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Unauthorized
        }

        User userProfile = userService.findByEmail(username);
        if (userProfile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // User not found
        }

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setId(userProfile.getId());
        userProfileDTO.setEmail(userProfile.getEmail());
        userProfileDTO.setUsername(userProfile.getUsername());
        return ResponseEntity.ok(userProfileDTO);
    }

    /**
     * Updates the profile of the currently authenticated user.
     *
     * @param userUpdatesDTO the data transfer object containing updated user
     *                       information (email, username).
     * @return a {@link ResponseEntity} containing the updated user's profile
     *         details as a {@link UserProfileDTO},
     *         or an error status if the user is not authenticated or not found.
     */
    @PutMapping("/profile")
    public ResponseEntity<UserProfileDTO> updateUserProfile(@RequestBody UserProfileDTO userUpdatesDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Unauthorized
        }

        User user = userService.findByEmail(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // User not found
        }

        User updatedUser = userService.updateUserProfile(user.getId(), userUpdatesDTO);
        UserProfileDTO updatedUserProfileDTO = new UserProfileDTO();
        updatedUserProfileDTO.setId(updatedUser.getId());
        updatedUserProfileDTO.setEmail(updatedUser.getEmail());
        updatedUserProfileDTO.setUsername(updatedUser.getUsername());
        return ResponseEntity.ok(updatedUserProfileDTO);
    }
}
