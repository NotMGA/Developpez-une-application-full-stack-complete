package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.model.Comment;

import java.util.List;

public interface CommentService {
    Comment addComment(Long postId, Long userId, String content);

    List<Comment> getCommentsByPost(Long postId);
}
