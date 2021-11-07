package com.example.fitnes.repository;

import com.example.fitnes.models.*;
import org.hibernate.jdbc.Work;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface WorkerRepository extends CrudRepository<Worker,Long> {
    Worker findByLogin(String login);
    //Worker findByPhone_listAndEmployee_list (Phone phone,Employee employee);

    @Query(value = "SELECT * FROM `worker` WHERE phone_list_id = ?1 and employee_list_id = ?2", nativeQuery = true)
    Worker findByPhonelistAndEmployeelist(Long idphone,Long idemployee);

    @Query(value = "SELECT * FROM `worker`INNER JOIN post on post_list_id = post.id WHERE post.name = 'Тренер'",nativeQuery = true)
    Iterable<Worker> findByPostCoach();
}
