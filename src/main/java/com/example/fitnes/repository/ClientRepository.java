package com.example.fitnes.repository;

import com.example.fitnes.models.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client,Long> {
}

