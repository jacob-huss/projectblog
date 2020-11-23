package com.tts.blog.repository;

import java.util.List;

import com.tts.blog.model.Post;
import com.tts.blog.model.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findAllByUserOrderByCreatedAtDesc(User user);
    List<Post> findAllByUserInOrderByCreatedAtDesc(List<User> users);
    List<Post> findByTags_PhraseOrderByCreatedAtDesc(String phrase);
}
