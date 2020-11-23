package com.tts.blog.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;


@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tag_id")
    private Long id;

    private String phrase;

    @ManyToMany(mappedBy = "tags")
    private List<Post> posts;

    public Tag (){}

    public Long getId() {
    return id;
    }

    public String getPhrase() {
    return phrase;
    }

    public void setPhrase(String phrase) {
    this.phrase = phrase;
    }

    public List<Post> getPosts() {
    return posts;
    }

    public void setTweets(List<Post> posts) {
    this.posts = posts;
    }

    @Override
    public String toString() {
    return "Tag [id=" + id + ", phrase=" + phrase + ", posts=" + posts + "]";
    }

}
