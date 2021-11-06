package com.example.fitnes.repository;

import com.example.fitnes.models.Client;
import com.example.fitnes.models.TrainingSchedule;
import com.example.fitnes.models.Worker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface TrainingScheduleRepository extends CrudRepository<TrainingSchedule,Long> {
    @Query(value = "SELECT * FROM `trainingschedule` INNER JOIN worker ON work_list_id = worker.id INNER JOIN post ON post_list_id = post.id WHERE post.name = 'Тренер' and DATEDIFF(?1,date) >= 0", nativeQuery = true)
    ArrayList<TrainingSchedule> findByAllDate(String date);
}
