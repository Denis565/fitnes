package com.example.fitnes.repository;

import com.example.fitnes.models.Client;
import com.example.fitnes.models.Employee;
import com.example.fitnes.models.Worker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client,Long> {
    Client findBySurnameAndNameAndPatronymic (String surname, String name, String patronymic);

    @Query(value = "SELECT * FROM `worker` WHERE phone_list_id = ?1 and employee_list_id = ?2", nativeQuery = true)
    Client findByPhonelist(Long idphone);
}

