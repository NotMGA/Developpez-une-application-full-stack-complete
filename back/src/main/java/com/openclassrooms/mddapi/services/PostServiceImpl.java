package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.CreatePostDTO;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Subject;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
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

    @Override
    public List<Post> getUserFeed(Long userId) {
        return postRepository.findAllByAuthor_Id(userId);
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post non trouvé"));
    }
}
