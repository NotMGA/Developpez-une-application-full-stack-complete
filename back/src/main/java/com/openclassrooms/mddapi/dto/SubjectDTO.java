package com.openclassrooms.mddapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data // Génère les getters, setters, toString, equals, hashCode
@NoArgsConstructor // Génère un constructeur sans arguments
@AllArgsConstructor // Génère un constructeur avec tous les arguments
@Builder // Permet d'utiliser le pattern Builder pour créer des instances de SubjectDTO
public class SubjectDTO {
    private Long id;
    private String name;
    private String description;
}
