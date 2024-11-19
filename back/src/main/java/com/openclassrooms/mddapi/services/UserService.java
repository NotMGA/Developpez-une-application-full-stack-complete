package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.UserProfileDTO;
import com.openclassrooms.mddapi.model.User;

public interface UserService {
    User registerUser(User user);

    User findByEmail(String email);

    boolean loginUser(String email, String password);

    User getUserProfile(Long userId);

    User updateUserProfile(Long userId, UserProfileDTO userUpdatesDTO);
}
