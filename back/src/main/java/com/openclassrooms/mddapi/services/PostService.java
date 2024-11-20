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

@Service
public class PostService {

    private final PostRepository postRepository;
    private final SubjectRepository subjectRepository;

    public PostService(PostRepository postRepository, SubjectRepository subjectRepository) {
        this.postRepository = postRepository;
        this.subjectRepository = subjectRepository;
    }

    public Post createPost(CreatePostDTO createPostDTO, User author) {
        // Assigner le sujet au post
        Subject subject = subjectRepository.findById(createPostDTO.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Sujet non trouvé"));

        // Créer un nouveau Post avec les informations du DTO
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
}
