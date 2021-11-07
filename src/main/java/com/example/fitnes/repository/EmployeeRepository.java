package com.example.fitnes.repository;

import com.example.fitnes.models.Client;
import com.example.fitnes.models.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee,Long> {
    Employee findBySurnameAndNameAndPatronymic (String surname,String name, String patronymic);

    List<Employee> findBySurnameContaining (String surname);
}
