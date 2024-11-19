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

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    // Endpoint d'inscription
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

    // Endpoint de connexion
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

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Unauthorized
        }

        User userProfile = userService.findByEmail(username);
        if (userProfile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Utilisateur non trouvé
        }

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setId(userProfile.getId());
        userProfileDTO.setEmail(userProfile.getEmail());
        userProfileDTO.setUsername(userProfile.getUsername());
        return ResponseEntity.ok(userProfileDTO);
    }

    // Endpoint pour mettre à jour le profil de l'utilisateur connecté
    @PutMapping("/profile")
    public ResponseEntity<UserProfileDTO> updateUserProfile(@RequestBody UserProfileDTO userUpdatesDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Unauthorized
        }

        User user = userService.findByEmail(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Utilisateur non trouvé
        }

        User updatedUser = userService.updateUserProfile(user.getId(), userUpdatesDTO);
        UserProfileDTO updatedUserProfileDTO = new UserProfileDTO();
        updatedUserProfileDTO.setId(updatedUser.getId());
        updatedUserProfileDTO.setEmail(updatedUser.getEmail());
        updatedUserProfileDTO.setUsername(updatedUser.getUsername());
        return ResponseEntity.ok(updatedUserProfileDTO);
    }
}
