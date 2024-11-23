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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null || username.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Unauthorized
        }

        User user = userService.findByEmail(username);
        if (user == null || user.getId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // User not found
        }

        if (subjectId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Subject ID not provided
        }

        createPostDTO.setSubjectId(subjectId);

        Post createdPost;
        try {
            createdPost = postService.createPost(createPostDTO, user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // User not found
        }

        List<Post> userFeed = postService.getCombinedUserFeed(user.getId());
        List<PostDTO> userFeedDTO = userFeed.stream().map(this::convertToDTO).collect(Collectors.toList());

        return ResponseEntity.ok(userFeedDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Post not found
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
