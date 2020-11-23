package com.tts.blog.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.tts.blog.model.Post;
import com.tts.blog.model.PostDisplay;
import com.tts.blog.model.User;
import com.tts.blog.service.PostService;
import com.tts.blog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostController {
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @GetMapping(value = { "/posts", "/" })
    public String getFeed(@RequestParam(value = "filter", required = false) String filter, Model model) {
        User loggedInUser = userService.getLoggedInUser();
        List<PostDisplay> posts = new ArrayList<>();
        if (filter == null) {
            filter = "all";
        }
        if (filter.equalsIgnoreCase("following")) {
            List<User> following = loggedInUser.getFollowing();
            posts = postService.findAllByUsers(following);
            model.addAttribute("filter", "following");
        } else {
            posts = postService.findAll();
            model.addAttribute("filter", "all");
        }
        model.addAttribute("postList", posts);
        return "feed";
    }

    @GetMapping(value = "/posts/{tag}")
    public String getTweetsByTag(@PathVariable(value = "tag") String tag, Model model) {
        List<PostDisplay> posts = postService.findAllWithTag(tag);
        model.addAttribute("postList", posts);
        model.addAttribute("tag", tag);
        return "taggedPosts";
    }

    @GetMapping(value = "/posts/new")
    public String getPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "newPost";
    }

    @PostMapping(value = "/posts")
    public String submitPostForm(@Valid Post post, BindingResult bindingResult, Model model) {
        User user = userService.getLoggedInUser();
        if (!bindingResult.hasErrors()) {
            post.setUser(user);
            postService.save(post);
            model.addAttribute("successMessage", "Post successfully created!");
            model.addAttribute("post", new Post());

        }
        return "newPost";
    }

}
