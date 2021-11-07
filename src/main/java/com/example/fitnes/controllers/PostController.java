package com.example.fitnes.controllers;

import com.example.fitnes.models.Client;
import com.example.fitnes.models.Post;
import com.example.fitnes.models.Service;
import com.example.fitnes.repository.ClientRepository;
import com.example.fitnes.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/")
    public String postview(Model model){
        Iterable<Post> post = postRepository.findAll();
        model.addAttribute("allpost", post);
        return  "post/post-view";
    }

    @PostMapping("post-view/{id}/del")
    public String deletepost(@PathVariable(value = "id") Long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);
        return "redirect:/post/";
    }

    @GetMapping("/add")
    public String postaddview(Post post, Model model) {
        return "post/post-add";
    }

    @PostMapping("/add")
    public String postadd(@Valid Post post, BindingResult bindingResult, Model model) {
        boolean errorB = true;
        if (bindingResult.hasErrors()){
            errorB = false;
        }
        if (postRepository.findByName(post.getName()) != null){
            ObjectError error = new ObjectError("name","Такая должность уже существует.");
            bindingResult.addError(error);
            errorB = false;
        }
        if (!errorB){
            return "post/post-add";
        }
        postRepository.save(post);
        return "redirect:/post/";
    }

    @GetMapping("/post-view/{id}/edit")
    public String editpostview(Post post, @PathVariable(value = "id") Long id, Model model) {
        if (!postRepository.existsById(id)) {
            return "redirect:/post/";
        }
        Post postS = postRepository.findById(id).orElseThrow();
        post.setName(postS.getName());
        post.setSalary(postS.getSalary());
        return "/post/edit-post";
    }

    @PostMapping("/post-view/{id}/edit")
    public String editpost(
            @Valid Post post, BindingResult bindingResult,
            @PathVariable(value = "id") Long id, Model model) {

        boolean errorB = true;
        if (bindingResult.hasErrors()){
            errorB = false;
        }

        Post posts = postRepository.findByName(post.getName());

        if (posts != null && !posts.getId().equals(postRepository.findById(id).orElseThrow().getId())){
            ObjectError error = new ObjectError("name","Такая должность уже существует.");
            bindingResult.addError(error);
            errorB = false;
        }
        if (!errorB){
            return "post/edit-post";
        }

        postRepository.save(post);
        return "redirect:/post/";
    }
}
