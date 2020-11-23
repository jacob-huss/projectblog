package com.tts.blog.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

@Entity
public class Post {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "post_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "post_tag", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags;

	@NotEmpty(message = "Post cannot be empty")
	@Length(max = 3000, message = "Post cannot have more than 3000 characters")
	private String message;

	@CreationTimestamp
    private Date createdAt;
    
    public Post(){}

	
	public Long getId() {
	return id;
	}

	public User getUser() {
	return user;
	}

	public void setUser(User user) {
	this.user = user;
	}

	public List<Tag> getTags() {
	return tags;
	}

	public void setTags(List<Tag> tags) {
	this.tags = tags;
	}

	public String getMessage() {
	return message;
	}

	public void setMessage(String message) {
	this.message = message;
	}

	public Date getCreatedAt() {
	return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
	this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Post [createdAt=" + createdAt + ", id=" + id + ", message=" + message + ", tags=" + tags + ", user="
                + user + "]";
    }
    

	
}
