package com.tts.blog.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tts.blog.model.Post;
import com.tts.blog.model.PostDisplay;
import com.tts.blog.model.Tag;
import com.tts.blog.model.User;
import com.tts.blog.repository.PostRepository;
import com.tts.blog.repository.TagRepository;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    public List<PostDisplay> findAll() {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        return formatPosts(posts);
    }

    public List<PostDisplay> findAllByUser(User user) {
        List<Post> posts = postRepository.findAllByUserOrderByCreatedAtDesc(user);
        return formatPosts(posts);
    }

    public List<PostDisplay> findAllByUsers(List<User> users) {
        List<Post> posts = postRepository.findAllByUserInOrderByCreatedAtDesc(users);
        return formatPosts(posts);
    }

    public List<PostDisplay> findAllWithTag(String tag) {
        List<Post> posts = postRepository.findByTags_PhraseOrderByCreatedAtDesc(tag);
        return formatPosts(posts);
    }

    public void save(Post post) {
        handleTags(post);
        postRepository.save(post);
    }

    private void handleTags(Post post) {
        List<Tag> tags = new ArrayList<Tag>();
        Pattern pattern = Pattern.compile("#\\w+");
        Matcher matcher = pattern.matcher(post.getMessage());
        while (matcher.find()) {
            String phrase = matcher.group().substring(1).toLowerCase();
            Tag tag = tagRepository.findByPhrase(phrase);
            if (tag == null) {
                tag = new Tag();
                tag.setPhrase(phrase);
                tagRepository.save(tag);
            }
            tags.add(tag);
        }
        post.setTags(tags);
    }

    private List<PostDisplay> formatPosts(List<Post> posts) {
        addTagLinks(posts);
        shortenLinks(posts);
        List<PostDisplay> displayPosts = formatTimestamps(posts);
        return displayPosts;
    }

    private void addTagLinks(List<Post> posts) {
        Pattern pattern = Pattern.compile("#\\w+");
        for (Post post : posts) {
            String message = post.getMessage();
            Matcher matcher = pattern.matcher(message);
            Set<String> tags = new HashSet<String>();
            while (matcher.find()) {
                tags.add(matcher.group());
            }
            for (String tag : tags) {
                message = message.replaceAll(tag,
                        "<a class=\"tag\" href=\"/posts/" + tag.substring(1).toLowerCase() + "\">" + tag + "</a>");
            }
            post.setMessage(message);
        }
    }

    private void shortenLinks(List<Post> posts) {
        Pattern pattern = Pattern.compile("https?[^ ]+");
        for (Post post : posts) {
            String message = post.getMessage();
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                String link = matcher.group();
                String shortenedLink = link;
                if (link.length() > 23) {
                    shortenedLink = link.substring(0, 20) + "...";
                    message = message.replace(link,
                            "<a class=\"tag\" href=\"" + link + "\" target=\"_blank\">" + shortenedLink + "</a>");
                }
                post.setMessage(message);
            }

        }
    }

    private List<PostDisplay> formatTimestamps(List<Post> posts) {
        List<PostDisplay> response = new ArrayList<>();
        PrettyTime prettyTime = new PrettyTime();
        SimpleDateFormat simpleDate = new SimpleDateFormat("M/d/yy");
        Date now = new Date();
        for (Post post : posts) {
            PostDisplay postDisplay = new PostDisplay();
            postDisplay.setUser(post.getUser());
            postDisplay.setMessage(post.getMessage());
            postDisplay.setTags(post.getTags());
            long diffInMillies = Math.abs(now.getTime() - post.getCreatedAt().getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if (diff > 3) {
                postDisplay.setDate(simpleDate.format(post.getCreatedAt()));
            } else {
                postDisplay.setDate(prettyTime.format(post.getCreatedAt()));
            }
            response.add(postDisplay);
        }
        return response;
    }

}
