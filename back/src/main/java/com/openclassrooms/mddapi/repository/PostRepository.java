package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findBySubjectInOrderByCreatedAtDesc(List<Subject> subjects);

    List<Post> findAllByAuthor_Id(Long userId);
}
