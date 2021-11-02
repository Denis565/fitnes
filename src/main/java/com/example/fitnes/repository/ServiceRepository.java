package com.example.fitnes.repository;

import com.example.fitnes.models.Client;
import com.example.fitnes.models.Service;
import org.springframework.data.repository.CrudRepository;

public interface ServiceRepository extends CrudRepository<Service,Long> {
}
