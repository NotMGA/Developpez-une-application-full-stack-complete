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

/**
 * REST controller for managing posts. Provides endpoints to create posts,
 * retrieve user feeds, and fetch posts by ID.
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    /**
     * Creates a new post for a given subject.
     *
     * @param subjectId     the ID of the subject associated with the post.
     * @param createPostDTO the data transfer object containing post details.
     * @return a {@link ResponseEntity} containing the created post as a DTO, or an
     *         error status if the operation fails.
     */
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestParam Long subjectId, @RequestBody CreatePostDTO createPostDTO) {
        // Get the email of the authenticated user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Unauthorized
        }

        // Find the user by email
        User user = userService.findByEmail(username);
        if (user == null || user.getId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // User not found
        }

        // Check if subjectId is null
        if (subjectId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Subject ID not provided
        }

        // Assign subjectId to createPostDTO
        createPostDTO.setSubjectId(subjectId);

        // Create the post
        Post createdPost;
        try {
            createdPost = postService.createPost(createPostDTO, user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Error creating post
        }

        return ResponseEntity.ok(convertToDTO(createdPost));
    }

    /**
     * Retrieves the authenticated user's post feed.
     *
     * @return a {@link ResponseEntity} containing a list of the user's posts as
     *         DTOs, or an error status if the operation fails.
     */
    @GetMapping("/feed")
    public ResponseEntity<List<PostDTO>> getUserFeed() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Unauthorized
        }

        User user = userService.findByEmail(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // User not found
        }

        List<Post> userFeed = postService.getUserFeed(user.getId());
        List<PostDTO> userFeedDTO = userFeed.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(userFeedDTO);
    }

    /**
     * Retrieves a post by its ID.
     *
     * @param id the ID of the post to retrieve.
     * @return a {@link ResponseEntity} containing the post as a DTO, or a 404
     *         status if the post is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Post not found
        }
        return ResponseEntity.ok(convertToDTO(post));
    }

    /**
     * Converts a {@link Post} entity to a {@link PostDTO}.
     *
     * @param post the post entity to convert.
     * @return the converted post as a DTO.
     */
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
