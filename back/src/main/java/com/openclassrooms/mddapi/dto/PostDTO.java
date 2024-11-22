package com.openclassrooms.mddapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data // Génère les getters, setters, toString, equals, hashCode
@NoArgsConstructor // Génère un constructeur sans arguments
@AllArgsConstructor // Génère un constructeur avec tous les arguments
@Builder // Permet d'utiliser le pattern Builder pour créer des instances de PostDTO
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private String subjectName;
    private String createdAt; // Format de la date en String pour lisibilité
}
