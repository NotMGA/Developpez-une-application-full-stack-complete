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

    /**
     * Adds a comment to a specific post.
     *
     * @param commentDTO the data transfer object containing the comment details.
     * @return a {@link ResponseEntity} containing the created comment as a DTO, or
     *         an error status if the operation fails.
     * @throws RuntimeException if an unexpected error occurs while adding the
     *                          comment.
     */
    @PostMapping
    public ResponseEntity<CommentDTO> addComment(@RequestBody CommentDTO commentDTO) {
        // Get the currently authenticated user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // If the user is not authenticated, return an unauthorized response
        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Find the user by email
        User user = userService.findByEmail(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // User not found
        }

        try {
            // Add the comment with the authenticated user's ID
            Comment comment = commentService.addComment(commentDTO.getPostId(), user.getId(), commentDTO.getContent());

            // Convert the Comment to CommentDTO for the response
            CommentDTO responseDTO = convertToDTO(comment);

            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Handle an unexpected error
        }
    }

    /**
     * Retrieves all comments for a specific post.
     *
     * @param postId the ID of the post whose comments are to be retrieved.
     * @return a {@link ResponseEntity} containing a list of comments as DTOs for
     *         the specified post.
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPost(postId);
        List<CommentDTO> commentDTOs = comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(commentDTOs);
    }

    /**
     * Converts a {@link Comment} entity to a {@link CommentDTO}.
     *
     * @param comment the comment entity to convert.
     * @return the converted comment as a DTO.
     */
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
