package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for managing comments, including adding comments and retrieving
 * comments for a specific post.
 */
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * Constructs a CommentService with the required dependencies.
     *
     * @param commentRepository the repository for comment-related database
     *                          operations.
     * @param postRepository    the repository for post-related database operations.
     * @param userRepository    the repository for user-related database operations.
     */
    public CommentService(CommentRepository commentRepository,
            PostRepository postRepository,
            UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    /**
     * Adds a comment to a specific post by a specific user.
     *
     * @param postId  the ID of the post to which the comment is being added.
     * @param userId  the ID of the user adding the comment.
     * @param content the content of the comment.
     * @return the added comment.
     * @throws RuntimeException if the post or user is not found.
     */
    public Comment addComment(Long postId, Long userId, String content) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setPost(post);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    /**
     * Retrieves all comments associated with a specific post, ordered by creation
     * date in ascending order.
     *
     * @param postId the ID of the post whose comments are to be retrieved.
     * @return a list of comments for the specified post.
     * @throws RuntimeException if the post is not found.
     */
    public List<Comment> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        return commentRepository.findByPostOrderByCreatedAtAsc(post);
    }
}
