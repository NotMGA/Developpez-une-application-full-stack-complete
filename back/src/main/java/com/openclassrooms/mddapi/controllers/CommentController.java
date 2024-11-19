package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.services.CommentService;
import com.openclassrooms.mddapi.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<CommentDTO> addComment(@RequestBody CommentDTO commentDTO) {
        // Obtenir l'utilisateur actuellement authentifié
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Si l'utilisateur n'est pas authentifié, retourner une réponse non autorisée
        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Rechercher l'utilisateur par email
        User user = userService.findByEmail(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Utilisateur non trouvé
        }

        try {
            // Ajouter le commentaire avec l'ID de l'utilisateur authentifié
            Comment comment = commentService.addComment(commentDTO.getPostId(), user.getId(), commentDTO.getContent());

            // Convertir le Comment en CommentDTO pour la réponse
            CommentDTO responseDTO = convertToDTO(comment);

            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Gestion d'une erreur inattendue
        }
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPost(postId);
        List<CommentDTO> commentDTOs = comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(commentDTOs);
    }

    // Méthode de conversion Comment vers CommentDTO
    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setPostId(comment.getPost().getId());
        dto.setContent(comment.getContent());
        dto.setAuthor(comment.getAuthor().getUsername());
        dto.setCreatedAt(comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return dto;
    }
}
