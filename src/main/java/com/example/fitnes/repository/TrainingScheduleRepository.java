package com.example.fitnes.repository;

import com.example.fitnes.models.Client;
import com.example.fitnes.models.TrainingSchedule;
import org.springframework.data.repository.CrudRepository;

public interface TrainingScheduleRepository extends CrudRepository<TrainingSchedule,Long> {
}
