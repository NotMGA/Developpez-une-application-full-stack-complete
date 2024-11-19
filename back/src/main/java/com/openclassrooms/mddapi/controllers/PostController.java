package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.CreatePostDTO;
import com.openclassrooms.mddapi.dto.PostDTO;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.services.PostService;
import com.openclassrooms.mddapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestParam Long subjectId, @RequestBody CreatePostDTO createPostDTO) {
        // Obtenir l'email de l'utilisateur authentifié
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Unauthorized
        }

        // Rechercher l'utilisateur par email
        User user = userService.findByEmail(username);
        if (user == null || user.getId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Utilisateur non trouvé
        }

        // Vérifiez si l'ID du sujet est null
        if (subjectId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Subject ID non fourni
        }

        // Assigner subjectId à createPostDTO
        createPostDTO.setSubjectId(subjectId);

        // Créer le post
        Post createdPost;
        try {
            createdPost = postService.createPost(createPostDTO, user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Erreur lors de la création du
                                                                                       // post
        }

        return ResponseEntity.ok(convertToDTO(createdPost));
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostDTO>> getUserFeed() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Unauthorized
        }

        User user = userService.findByEmail(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Utilisateur non trouvé
        }

        List<Post> userFeed = postService.getUserFeed(user.getId());
        List<PostDTO> userFeedDTO = userFeed.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(userFeedDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Post non trouvé
        }
        return ResponseEntity.ok(convertToDTO(post));
    }

    private PostDTO convertToDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setAuthorName(post.getAuthor().getUsername());
        postDTO.setSubjectName(post.getSubject().getName());
        postDTO.setCreatedAt(post.getCreatedAt().toString());
        return postDTO;
    }
}
