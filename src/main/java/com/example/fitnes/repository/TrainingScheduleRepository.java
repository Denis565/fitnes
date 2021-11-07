package com.example.fitnes.repository;

import com.example.fitnes.models.Client;
import com.example.fitnes.models.TrainingSchedule;
import com.example.fitnes.models.Worker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface TrainingScheduleRepository extends CrudRepository<TrainingSchedule,Long> {
    @Query(value = "SELECT * FROM `trainingschedule` WHERE  DATEDIFF(CURRENT_DATE(),date) <= 0;", nativeQuery = true)
    ArrayList<TrainingSchedule> findByAllDate();

    @Query(value = "SELECT * FROM `trainingschedule` " +
            "INNER JOIN worker on work_list_id = worker.id" +
            " WHERE (start_time <= ?2) and (?1 <= end_time) and (DATEDIFF(?3,date) <= 0 and (worker.id = ?4))",nativeQuery = true)
    ArrayList<TrainingSchedule> findByTimeRangesAreOverlaping(String startTimeSelected,String endTimeSelected,String dateSelected,Long idWorker);
}
