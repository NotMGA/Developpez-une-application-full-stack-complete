package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.CreatePostDTO;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Subject;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, SubjectRepository subjectRepository,
            UserRepository userRepository) {
        this.postRepository = postRepository;
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
    }

    public Post createPost(CreatePostDTO createPostDTO, User author) {
        Subject subject = subjectRepository.findById(createPostDTO.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Sujet non trouvé"));

        Post post = new Post();
        post.setTitle(createPostDTO.getTitle());
        post.setContent(createPostDTO.getContent());
        post.setSubject(subject);
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());

        return postRepository.save(post);
    }

    public List<Post> getUserFeed(Long userId) {
        return postRepository.findAllByAuthor_Id(userId);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post non trouvé"));
    }

    /**
     * Retrieves posts related to the subjects the user is subscribed to.
     * 
     * @param userId the ID of the user
     * @return a list of posts related to the user's subscriptions
     */
    public List<Post> getUserFeedBySubscriptions(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Récupérer les abonnements de l'utilisateur
        List<Subject> subscriptions = user.getSubscriptions();

        if (subscriptions == null || subscriptions.isEmpty()) {
            return List.of(); // Retourner une liste vide si l'utilisateur n'a aucun abonnement
        }

        // Récupérer les posts associés aux sujets auxquels l'utilisateur est abonné
        return postRepository.findBySubjectInOrderByCreatedAtDesc(subscriptions);
    }

    /**
     * Retrieves a combined feed of posts created by the user and posts from
     * subjects the user is subscribed to.
     * 
     * @param userId the ID of the user
     * @return a combined list of posts
     */
    public List<Post> getCombinedUserFeed(Long userId) {
        // Récupérer les posts créés par l'utilisateur
        List<Post> userPosts = getUserFeed(userId);

        // Récupérer les posts basés sur les abonnements de l'utilisateur
        List<Post> subscriptionPosts = getUserFeedBySubscriptions(userId);

        // Combiner les deux listes
        List<Post> combinedFeed = new ArrayList<>();
        combinedFeed.addAll(userPosts);
        combinedFeed.addAll(subscriptionPosts);

        return combinedFeed;
    }
}
