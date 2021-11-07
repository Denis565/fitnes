package com.example.fitnes.repository;

import com.example.fitnes.models.Client;
import com.example.fitnes.models.Employee;
import com.example.fitnes.models.Worker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client,Long> {
    Client findBySurnameAndNameAndPatronymic (String surname, String name, String patronymic);
}


