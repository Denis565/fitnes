package com.example.fitnes.repository;

import com.example.fitnes.models.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post,Long> {
}
