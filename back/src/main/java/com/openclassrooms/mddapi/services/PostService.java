package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.CreatePostDTO;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.User;

import java.util.List;

public interface PostService {
    Post createPost(CreatePostDTO createPostDTO, User author);

    List<Post> getUserFeed(Long userId);

    Post getPostById(Long id);
}
