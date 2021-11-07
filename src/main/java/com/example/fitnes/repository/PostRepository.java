package com.example.fitnes.repository;

import com.example.fitnes.models.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface PostRepository extends CrudRepository<Post,Long> {
    Post findByName (String name);
}
