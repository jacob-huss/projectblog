package com.tts.blog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tts.blog.model.Tag;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long>  {
    
	Tag findByPhrase(String phrase);

}
