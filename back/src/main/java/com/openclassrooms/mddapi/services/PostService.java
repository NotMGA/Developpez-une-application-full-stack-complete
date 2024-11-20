package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.CreatePostDTO;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Subject;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for managing posts, including creation, retrieval, and user-specific
 * feeds.
 */
@Service
public class PostService {

    private final PostRepository postRepository;
    private final SubjectRepository subjectRepository;

    /**
     * Constructs a PostService with the required dependencies.
     *
     * @param postRepository    the repository for post-related database operations.
     * @param subjectRepository the repository for subject-related database
     *                          operations.
     */
    public PostService(PostRepository postRepository, SubjectRepository subjectRepository) {
        this.postRepository = postRepository;
        this.subjectRepository = subjectRepository;
    }

    /**
     * Creates a new post.
     *
     * @param createPostDTO the data transfer object containing the post details.
     * @param author        the user who is the author of the post.
     * @return the created post.
     * @throws RuntimeException if the subject associated with the post is not
     *                          found.
     */
    public Post createPost(CreatePostDTO createPostDTO, User author) {
        // Assign the subject to the post
        Subject subject = subjectRepository.findById(createPostDTO.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Sujet non trouvé"));

        // Create a new Post with the information from the DTO
        Post post = new Post();
        post.setTitle(createPostDTO.getTitle());
        post.setContent(createPostDTO.getContent());
        post.setSubject(subject);
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());

        return postRepository.save(post);
    }

    /**
     * Retrieves a feed of posts created by a specific user.
     *
     * @param userId the ID of the user whose posts are to be retrieved.
     * @return a list of posts created by the specified user.
     */
    public List<Post> getUserFeed(Long userId) {
        return postRepository.findAllByAuthor_Id(userId);
    }

    /**
     * Retrieves a post by its ID.
     *
     * @param id the ID of the post to be retrieved.
     * @return the post with the specified ID.
     * @throws RuntimeException if the post is not found.
     */
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post non trouvé"));
    }
}
