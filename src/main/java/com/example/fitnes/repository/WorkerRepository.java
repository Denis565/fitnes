package com.example.fitnes.repository;

import com.example.fitnes.models.Client;
import com.example.fitnes.models.Worker;
import org.springframework.data.repository.CrudRepository;

public interface WorkerRepository extends CrudRepository<Worker,Long> {
    Worker findByLogin(String login);
}
