package com.openclassrooms.mddapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data // Génère les getters, setters, toString, equals, hashCode
@NoArgsConstructor // Génère un constructeur sans arguments
@AllArgsConstructor // Génère un constructeur avec tous les arguments
@Builder // Permet d'utiliser le pattern Builder pour créer des instances de
         // UserProfileDTO
public class UserProfileDTO {
    private Long id;
    private String email;
    private String username;
}
